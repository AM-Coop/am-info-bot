package ru.am.aminfobot.service.bot

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import ru.am.aminfobot.model.enums.AmRole
import ru.am.aminfobot.model.enums.ChatState
import ru.am.aminfobot.repo.AmUserRepository
import ru.am.aminfobot.repo.document.AmUser
import ru.am.aminfobot.service.file.ConfigLoaderService

@Component
class AmInfoTgBot(
    private val configLoaderService: ConfigLoaderService,
    private val amUserRepository: AmUserRepository
) : TelegramLongPollingBot(configLoaderService.loadTgProps().token) {

    private val log = KotlinLogging.logger { }

    @PostConstruct
    fun init() {
        TelegramBotsApi(DefaultBotSession::class.java).also {
            it.registerBot(this)
        }
    }

    override fun getBotUsername(): String {
        return configLoaderService.loadTgProps().name
    }

    override fun onUpdateReceived(update: Update) {
        log.info("new msg received: $update")
        if (update.message?.text == "/start") sendMsg("chatId: ${update.message.chatId}", update.message.chatId.toString())
        process(update)
    }

    private fun process(update: Update) {
        amUserRepository.findByUserId(update.message.from.id)
            .publishOn(Schedulers.boundedElastic())
            .switchIfEmpty {
                log.info { "новый юзер" }
                sendMsg("Напиши свое духовное имя для регистрации", update.message.chatId.toString())
                amUserRepository.insert(
                    AmUser(
                        chatId = update.message.chatId,
                        username = update.message.from.userName,
                        userId = update.message.from.id,
                        currentState = ChatState.WAIT_FOR_REGISTRATION
                    )
                )
                    .then(Mono.empty())
            }
            .flatMap { operateOnState(update, it) }
            .subscribe()
    }

    private fun operateOnState(update: Update, user: AmUser): Mono<AmUser> {
        return when (user.currentState) {
            ChatState.WAIT_FOR_REGISTRATION -> {
                amUserRepository.save(
                    user.apply {
                        spiritualName = update.message.text
                        currentState = ChatState.NONE
                        roles = setOf(AmRole.GUEST)
                    }.also { log.info { "spiritualName [${update.message.text}] was updated for ${user.username}" } }
                ).doOnSuccess {
                    sendMsg("Регистрация успешна!", update.message.chatId.toString())
                }
            }
            else -> operateOnAction(update, user)
        }
    }

    private fun operateOnAction(update: Update, user: AmUser): Mono<AmUser> {
        if (update.message.text == "/send_schedule" && user.roles.contains(AmRole.SCHEDULE_MANAGER) && user.currentState == ChatState.NONE) {
            return amUserRepository.save(
                user.apply { currentState = ChatState.WAIT_FOR_SCHEDULE_DOC }
            ).doOnSuccess {
                sendMsg("Ожидание загрузки таблицы", update.message.chatId.toString())
            }
        } else if (user.roles.contains(AmRole.SCHEDULE_MANAGER) && user.currentState == ChatState.WAIT_FOR_SCHEDULE_DOC && update.message.hasDocument()) {
            val docId = update.message.document.fileId
            val docName = update.message.document.fileName
            val getID = java.lang.String.valueOf(update.message.from.id)
            val getFile = GetFile(docId)
            val file: File = execute<File, BotApiMethod<File>>(getFile)
            val outp = java.io.File("./data/userDoc/${getID}_$docName")
            downloadFile(file, outp)
            //todo send file to schedule-app
            return amUserRepository.save(user.apply { currentState = ChatState.NONE }).doOnSuccess {
                sendMsg("Таблица принята", update.message.chatId.toString())
                sendMenuForRole(update, user)
            }
        } else sendMenuForRole(update, user)
        return Mono.just(user)
    }

    private fun sendMenuForRole(update: Update, user: AmUser) {
        val sb = StringBuilder()
        sb.append("/start\n")
        if (user.roles.contains(AmRole.SCHEDULE_MANAGER))
            sb.append("/send_schedule\n")
        sendMsg(sb.toString(), update.message.chatId.toString())
    }

    fun sendMsg(s: String, chatId: String) {
        execute(
            SendMessage(
                chatId, s
            )
        )
    }
}
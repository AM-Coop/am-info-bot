package ru.am.aminfobot.service.bot

import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.am.aminfobot.model.entity.AmRole
import ru.am.aminfobot.model.entity.AmUser
import ru.am.aminfobot.model.enums.ChatState
import ru.am.aminfobot.repo.AmUserRepository
import ru.am.aminfobot.service.file.ConfigLoaderService

@Component
class AmInfoTgBot(
    private val configLoaderService: ConfigLoaderService,
    private val amUserRepository: AmUserRepository,
) : TelegramLongPollingBot(configLoaderService.loadTgProps().token) {

    private val log = KotlinLogging.logger { }

    @Value("\${ru.am.config.schedule-back.url}")
    private lateinit var scheduleBackUrl: String

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
        var user = amUserRepository.findByUserId(update.message.from.id)
        if (user == null) {
            log.info { "новый юзер" }
            sendMsg("Напиши свое духовное имя для регистрации", update.message.chatId.toString())
            user = amUserRepository.save(
                AmUser().apply {
                    chatId = update.message.chatId
                    username = update.message.from.userName
                    userId = update.message.from.id
                    currentState = ChatState.WAIT_FOR_REGISTRATION
                }

            )
        } else operateOnState(update, user)
    }

    private fun operateOnState(update: Update, user: AmUser): AmUser {
        return when (user.currentState) {
            ChatState.WAIT_FOR_REGISTRATION -> {
                amUserRepository.save(
                    user.apply {
                        spiritualName = update.message.text
                        currentState = ChatState.NONE
                    }.also { log.info { "spiritualName [${update.message.text}] was updated for ${user.username}" } }
                ).also {
                    sendMsg("Регистрация успешна!", update.message.chatId.toString())
                    sendMenuForRole(update, user)

                }
            }

            else -> operateOnAction(update, user)
        }
    }

    private fun operateOnAction(update: Update, user: AmUser): AmUser {
        // if (update.message.text == "/send_schedule" && user.roles.contains(AmRole.SCHEDULE_MANAGER) && user.currentState == ChatState.NONE) {
        //     return amUserRepository.save(
        //         user.apply { currentState = ChatState.WAIT_FOR_SCHEDULE_DOC }
        //     ).doOnSuccess {
        //         sendMsg("Ожидание загрузки таблицы", update.message.chatId.toString())
        //     }
        // } else if (user.roles.contains(AmRole.SCHEDULE_MANAGER) && user.currentState == ChatState.WAIT_FOR_SCHEDULE_DOC && update.message.hasDocument()) {
        //     val docId = update.message.document.fileId
        //     val docName = update.message.document.fileName
        //     val getID = java.lang.String.valueOf(update.message.from.id)
        //     val getFile = GetFile(docId)
        //     val file: File = execute<File, BotApiMethod<File>>(getFile)
        //     val outp = java.io.File("/tmp/wb/${getID}_$docName")
        //     downloadFile(file, outp)
        //
        //     log.info { "отправка файлика" }
        //     return Mono.defer {
        //         schedWebClient.get()
        //             .uri("http://localhost:8081/v2/refresh")
        //             // .contentType(MediaType.MULTIPART_FORM_DATA)
        //             // .body(BodyInserters.fromMultipartData(fromFile(outp)))
        //             .retrieve()
        //             .bodyToMono(String::class.java)
        //     }.then(
        //         amUserRepository.save(user.apply { currentState = ChatState.NONE }).doOnSuccess {
        //             sendMsg("Таблица принята", update.message.chatId.toString())
        //             sendMenuForRole(update, user)
        //         }
        //             .doOnError { log.error("some err -> ${it.message}", it) }
        //             .subscribeOn(Schedulers.boundedElastic())
        //     )
        //
        // } else if (update.message.text == "/result") {
        //     val res = java.io.File("/tmp/wb")
        //
        //     res.listFiles()?.firstOrNull { it.name.endsWith(".txt") }?.let {
        //
        //         FileInputStream(it).use { fis -> sendMsg(String(fis.readAllBytes()), update.message.chatId.toString()) }
        //         sendMenuForRole(update, user)
        //     }
        //
        // } else sendMenuForRole(update, user)
        // return Mono.just(user)
        //
        if (update.message.text == "/refresh" && AmRole.valueOf(user.userRole) in listOf(AmRole.SCHEDULE_MANAGER, AmRole.ADMIN)) {
            val restTemplate = RestTemplate()
            val fooResourceUrl = "$scheduleBackUrl/v2/refresh"
            val res = try {
                restTemplate.getForEntity("$fooResourceUrl", String::class.java).body ?: ""
            } catch (err: Exception) {
                log.error("err -> ${err.message}", err)
                err.message ?: ""
            }
            sendMsg(res, update.message.chatId.toString())
        }

        sendMenuForRole(update, user)


        return user
    }

    private fun fromFile(file: java.io.File): MultiValueMap<String?, HttpEntity<*>?> {
        val builder = MultipartBodyBuilder()
        builder.part("file", FileSystemResource(file))
        return builder.build()
    }

    private fun sendMenuForRole(update: Update, user: AmUser) {
        val sb = StringBuilder()
        sb.append("/start\n")
        if (AmRole.valueOf(user.userRole) in listOf(AmRole.SCHEDULE_MANAGER, AmRole.ADMIN)) {
            sb.append("/refresh\n")
            // sb.append("/result\n")
        }

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
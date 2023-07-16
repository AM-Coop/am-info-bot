package ru.am.aminfobot.service.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Update
import ru.am.aminfobot.service.file.TelegramProperties

class AmInfoTgBot(private val props: TelegramProperties) : TelegramLongPollingBot(props.token) {

    override fun getBotUsername(): String {
        return props.name
    }

    override fun onUpdateReceived(update: Update) {
        if (update.message?.text == "/start") {
            println("START")
            sendMsg("chatId: ${update.message.chatId}", update.message.chatId.toString())
            sendMsg("menu - /menu", update.message.chatId.toString())
        } else {
            if (update.message.hasDocument()) {
                val docId = update.message.document.fileId
                val docName = update.message.document.fileName
                val getID = java.lang.String.valueOf(update.message.from.id)
                val getFile = GetFile(docId)
                val file: File = execute<File, BotApiMethod<File>>(getFile)
                val outp = java.io.File("./data/userDoc/${getID}_$docName")
                downloadFile(file, outp)
            }
            println(update)
        }
    }

    fun sendMsg(s: String, chatId: String) {
        execute(
            SendMessage(
                chatId, s
            )
        )
    }
}
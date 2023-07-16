package ru.am.aminfobot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.am.aminfobot.service.bot.AmInfoTgBot
import ru.am.aminfobot.service.file.ConfigLoaderService

@Configuration
class TelegramConfig(
    private val configLoaderService: ConfigLoaderService
) {

    @Bean
    fun telegramBot() =
        AmInfoTgBot(configLoaderService.loadTgProps()).also { bot ->
            TelegramBotsApi(DefaultBotSession::class.java).also {
                it.registerBot(bot)
            }
        }
}
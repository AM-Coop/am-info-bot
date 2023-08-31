package ru.am.aminfobot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class AmInfoBotApplication

fun main(args: Array<String>) {
    runApplication<AmInfoBotApplication>(*args)
}

package ru.am.aminfobot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AmInfoBotApplication

fun main(args: Array<String>) {
    runApplication<AmInfoBotApplication>(*args)
}

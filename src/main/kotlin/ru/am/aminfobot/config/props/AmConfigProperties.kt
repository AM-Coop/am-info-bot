package ru.am.aminfobot.config.props

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "ru.am.config.telegram.auth-file-path")
class AmConfigProperties {
    var unix: String = ""
    var win: String = ""
    var tgAuthFilePath: String = ""

    @PostConstruct
    fun init() {
        val os = System.getProperty("os.name")
        tgAuthFilePath = if (os.contains("win")) win else unix
    }
}
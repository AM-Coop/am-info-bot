package ru.am.aminfobot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class RestConfig {

    @Bean
    fun schedWebClient() = WebClient.builder()
        .exchangeStrategies(
            ExchangeStrategies.builder()
                .codecs { codecs -> codecs.defaultCodecs().maxInMemorySize(500000) }
                .build()
        )
        .build()
}
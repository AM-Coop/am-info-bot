package ru.am.aminfobot.service.file

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import ru.am.aminfobot.config.props.AmConfigProperties
import java.io.File
import java.io.FileNotFoundException

@Service
class ConfigLoaderService(
    private val amConfigProperties: AmConfigProperties
) {
    private val tgPropsRef = object : TypeReference<TelegramProperties>() {}

    private val mapper = ObjectMapper().apply {
        registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        configure(SerializationFeature.INDENT_OUTPUT, true)
    }

    fun loadTgProps() =
        readFromJson(tgPropsRef, amConfigProperties.tgAuthFilePath)

    private fun <T> readFromJson(typeRef: TypeReference<T>, filePath: String): T {
        val file = File(filePath)
        if (!file.exists()) throw FileNotFoundException("File not found with path ${file.path}")
        if (file.isDirectory) throw IllegalStateException("${file.path} is a directory")
        return mapper.readValue(file.inputStream().readAllBytes(), typeRef)
    }
}
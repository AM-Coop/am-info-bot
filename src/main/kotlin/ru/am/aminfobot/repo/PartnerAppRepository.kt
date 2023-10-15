package ru.am.aminfobot.repo

import org.springframework.data.repository.CrudRepository
import ru.am.aminfobot.model.entity.PartnerApp
import java.util.UUID

interface PartnerAppRepository : CrudRepository<PartnerApp, UUID> {
    fun findByChatId(chatId: Long): PartnerApp?
}
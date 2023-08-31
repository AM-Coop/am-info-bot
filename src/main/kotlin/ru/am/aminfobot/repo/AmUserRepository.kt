package ru.am.aminfobot.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.am.aminfobot.model.entity.AmUser
import java.util.UUID

@Repository
interface AmUserRepository : CrudRepository<AmUser, UUID> {

    fun findByUserId(userId: Long): AmUser?
}
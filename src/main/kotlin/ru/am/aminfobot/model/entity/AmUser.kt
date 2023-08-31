package ru.am.aminfobot.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import ru.am.aminfobot.model.enums.ChatState
import java.time.LocalDateTime
import java.util.UUID

@Entity
class AmUser {
    @Id
    var id: UUID = UUID.randomUUID()
    var spiritualName: String? = null
    var chatId: Long? = null
    var username: String? = null
    var userId: Long? = null
    var userRole: String = AmRole.GUEST.name
    var currentState: ChatState = ChatState.NONE
    var createDate: LocalDateTime = LocalDateTime.now()
}
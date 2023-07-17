package ru.am.aminfobot.repo.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.am.aminfobot.model.enums.AmRole
import ru.am.aminfobot.model.enums.ChatState
import java.time.LocalDateTime

@Document
class AmUser(
    @Id var id: String? = null,
    var spiritualName: String? = null,
    var chatId: Long? = null,
    var username: String? = null,
    var userId: Long? = null,
    var roles: Set<AmRole> = setOf(),
    var currentState: ChatState = ChatState.NONE,
    var createDate: LocalDateTime = LocalDateTime.now()
)
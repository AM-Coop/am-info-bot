package ru.am.aminfobot.model.entity

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.UUID

@MappedSuperclass
abstract class Identity {
    @Id
    open var id: UUID = UUID.randomUUID()

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Identity -> false
        id != other.id -> false
        else -> true
    }

    override fun hashCode() = id.hashCode()
}
package ru.am.aminfobot.repo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import ru.am.aminfobot.repo.document.AmUser

@Repository
interface AmUserRepository : ReactiveMongoRepository<AmUser, Long> {

    fun findByUserId(userId: Long): Mono<AmUser>
}
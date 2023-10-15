package ru.am.aminfobot

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import ru.am.aminfobot.model.entity.PartnerApp
import ru.am.aminfobot.repo.PartnerAppRepository
import java.time.LocalDate

@SpringBootTest
class AmInfoBotApplicationTests {

    @Autowired
    private lateinit var partnerAppRepository: PartnerAppRepository

    @Test
    @DirtiesContext
    fun contextLoads() {
        partnerAppRepository.save(
            PartnerApp(
                12, "", ",", "", "", LocalDate.now(), "", "", "", LocalDate.now(), 11, false
            )
        )
        println(
            partnerAppRepository.findAll().first()
        )
    }
}

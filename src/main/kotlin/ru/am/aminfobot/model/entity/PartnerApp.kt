package ru.am.aminfobot.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "partner_apps")
class PartnerApp(
    var chatId: Long? = null,
    // chat_id    bigint                       NOT NULL,

    // secondname character varying(50)        NOT NULL,
    var secondname: String? = null,
    // firstname  character varying(50)        NOT NULL,
    var firstname: String? = null,

    // thirdname  character varying(50),
    var thirdname: String? = null,

    // spiritname character varying(50),
    var spiritname: String? = null,

    // birthdate  date                         NOT NULL,

    var birthdate: LocalDate? = null,

    // phone      character varying(10)        NOT NULL,
    var phone: String? = null,

    // login_tg   character varying(40),
    var loginTg: String? = null,

    // email      character varying(60),
    var email: String? = null,

    // app_date   date    DEFAULT CURRENT_DATE NOT NULL,
    var appDate: LocalDate? = null,

    // unit_id    bigint,
    var unitId: Long? = null,

    // has_lesson boolean DEFAULT false        NOT NULL,
    var hasLesson: Boolean? = null,

    ) : Identity() {

}


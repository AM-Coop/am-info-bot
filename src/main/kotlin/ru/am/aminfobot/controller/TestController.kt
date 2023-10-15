package ru.am.aminfobot.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("ok")
    }

    @PostMapping("/auth", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun receiveAuth(@RequestParam body: MultiValueMap<Any, Any>) {
        println("received: $body")
    }
}
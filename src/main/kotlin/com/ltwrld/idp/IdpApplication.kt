package com.ltwrld.idp

import com.ltwrld.idp.infrastructure.config.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class IdpApplication

fun main(args: Array<String>) {
	runApplication<IdpApplication>(*args)
}

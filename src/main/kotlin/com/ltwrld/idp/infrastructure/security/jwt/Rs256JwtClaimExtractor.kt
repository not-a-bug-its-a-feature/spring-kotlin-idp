package com.ltwrld.idp.infrastructure.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.ltwrld.idp.application.port.out.jwt.JwtClaimExtractor
import com.ltwrld.idp.infrastructure.security.jwt.model.JwtPayload
import com.ltwrld.idp.infrastructure.crypto.Base64Url
import org.springframework.stereotype.Component

@Component
class Rs256JwtClaimExtractor(
    private val objectMapper: ObjectMapper
) : JwtClaimExtractor {
    override fun extractClaim(token: String): JwtPayload {
        val parts = token.split(".")

        val payloadJson = String(Base64Url.decode(parts[1]))

        return objectMapper.readValue(payloadJson, JwtPayload::class.java)
    }
}
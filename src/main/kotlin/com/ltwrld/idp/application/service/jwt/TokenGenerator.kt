package com.ltwrld.idp.application.service.jwt

import com.ltwrld.idp.application.port.out.jwt.JwtSignerPort
import com.ltwrld.idp.domain.model.TokenClaim
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TokenGenerator(
    private val jwtSigner: JwtSignerPort
) {
    fun create(userId: String, aud: String, now: Instant, expiresIn: Long, nonce: String?): String {
        val claim = TokenClaim(
            sub = userId,
            aud = aud,
            iss = "dev-idp",
            iat = now.epochSecond,
            exp = now.epochSecond + expiresIn,
            nonce = nonce
        )

        return jwtSigner.sign(claim)
    }
}
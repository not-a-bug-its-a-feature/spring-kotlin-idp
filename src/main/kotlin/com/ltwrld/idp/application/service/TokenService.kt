package com.ltwrld.idp.application.service

import com.ltwrld.idp.application.port.`in`.IssueTokenUseCase
import com.ltwrld.idp.application.port.`in`.TokenPair
import com.ltwrld.idp.application.port.out.JwtSignerPort
import com.ltwrld.idp.domain.model.TokenClaim
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class TokenService(
    private val jwtSigner: JwtSignerPort
) : IssueTokenUseCase {
    override fun issue(userId: String): TokenPair {
        val now = Instant.now()

        val accessClaim = TokenClaim(
            sub = userId,
            aud = "dev-api",
            iss = "dev-idp",
            iat = now.epochSecond,
            exp = now.epochSecond + 180,
            nonce = null
        )

        val refreshClaim = TokenClaim(
            sub = userId,
            aud = "dev-api",
            iss = "dev-idp",
            iat = now.epochSecond,
            exp = now.epochSecond + 3600,
            nonce = UUID.randomUUID().toString()
        )

        val accessToken = jwtSigner.sign(accessClaim)
        val refreshToken = jwtSigner.sign(refreshClaim)

        return TokenPair(accessToken, refreshToken)
    }

}
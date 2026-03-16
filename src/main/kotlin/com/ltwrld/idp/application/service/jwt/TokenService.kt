package com.ltwrld.idp.application.service.jwt

import com.ltwrld.idp.application.port.`in`.jwt.IssueTokenUseCase
import com.ltwrld.idp.application.dto.TokenPair
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class TokenService(
    private val tokenGenerator: TokenGenerator

) : IssueTokenUseCase {
    override fun issue(userId: String): TokenPair {
        val now = Instant.now()

        val accessToken = tokenGenerator.create(
            userId = userId,
            aud = "dev-api",
            now = now,
            expiresIn = 180,
            nonce = null)

        val refreshToken = tokenGenerator.create(
            userId = userId,
            aud = "dev-api",
            now = now,
            expiresIn = 3600,
            nonce = UUID.randomUUID().toString()
        )
        return TokenPair(accessToken, refreshToken)
    }

}
package com.ltwrld.idp.application.service.jwt

import com.ltwrld.idp.application.dto.TokenPair
import com.ltwrld.idp.application.port.`in`.jwt.RefreshTokenUseCase
import com.ltwrld.idp.application.port.out.jwt.JwtClaimExtractor
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RefreshTokenService(
    private val jwtClaimExtractor: JwtClaimExtractor,
    private val tokenGenerator: TokenGenerator
): RefreshTokenUseCase {
    override fun refresh(refreshToken: String): TokenPair {
        val claims = jwtClaimExtractor.extractClaim(refreshToken)
        val now = Instant.now()

        val accessToken = tokenGenerator.create(
            userId = claims.sub,
            aud = claims.aud,
            now = now,
            expiresIn = 180,
            nonce = null
        )

        val refreshToken = tokenGenerator.create(
            userId = claims.sub,
            aud = claims.aud,
            now = now,
            expiresIn = 3600,
            nonce = claims.nonce
        )

        return TokenPair(accessToken, refreshToken)
    }
}
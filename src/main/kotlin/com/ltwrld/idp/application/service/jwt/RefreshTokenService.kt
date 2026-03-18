package com.ltwrld.idp.application.service.jwt

import com.ltwrld.idp.application.command.TokenIssueCommand
import com.ltwrld.idp.application.dto.TokenPair
import com.ltwrld.idp.application.model.TokenType
import com.ltwrld.idp.application.port.`in`.jwt.RefreshTokenUseCase
import com.ltwrld.idp.application.port.out.jwt.JwtClaimExtractor
import com.ltwrld.idp.application.port.out.jwt.JwtSignerPort
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RefreshTokenService(
    private val jwtClaimExtractor: JwtClaimExtractor,
    private val jwtSigner: JwtSignerPort
): RefreshTokenUseCase {
    override fun refresh(refreshToken: String): TokenPair {
        val claims = jwtClaimExtractor.extractClaim(refreshToken)

        val command = TokenIssueCommand(claims.sub, TokenType.ACCESS, Instant.now())

        val accessToken = jwtSigner.sign(command)

        val refreshToken = jwtSigner.sign(command.copy(type = TokenType.REFRESH))

        return TokenPair(accessToken, refreshToken)
    }
}
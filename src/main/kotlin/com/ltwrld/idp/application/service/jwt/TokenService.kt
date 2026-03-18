package com.ltwrld.idp.application.service.jwt

import com.ltwrld.idp.application.command.TokenIssueCommand
import com.ltwrld.idp.application.port.`in`.jwt.IssueTokenUseCase
import com.ltwrld.idp.application.dto.TokenPair
import com.ltwrld.idp.application.model.TokenType
import com.ltwrld.idp.application.port.out.jwt.JwtSignerPort
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class TokenService(
    private val jwtSigner: JwtSignerPort

) : IssueTokenUseCase {
    override fun issue(userId: String): TokenPair {

        val command = TokenIssueCommand(userId, TokenType.ACCESS, issuedAt= Instant.now())

        val accessToken = jwtSigner.sign(command)

        val refreshToken = jwtSigner.sign(command.copy(type = TokenType.REFRESH))
        return TokenPair(accessToken, refreshToken)
    }

}
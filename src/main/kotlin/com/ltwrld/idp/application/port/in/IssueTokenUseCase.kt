package com.ltwrld.idp.application.port.`in`

interface IssueTokenUseCase {
    fun issue(userId: String): TokenPair
}

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
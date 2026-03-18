package com.ltwrld.idp.application.command

import com.ltwrld.idp.application.model.TokenType
import java.time.Instant

data class TokenIssueCommand(
    val userId: String,
    val type: TokenType,
    val issuedAt: Instant
)

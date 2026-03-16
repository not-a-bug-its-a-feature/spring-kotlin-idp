package com.ltwrld.idp.application.port.`in`.jwt

import com.ltwrld.idp.application.dto.TokenPair

interface IssueTokenUseCase {
    fun issue(userId: String): TokenPair
}


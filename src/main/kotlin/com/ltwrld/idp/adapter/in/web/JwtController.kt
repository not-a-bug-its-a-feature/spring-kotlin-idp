package com.ltwrld.idp.adapter.`in`.web

import com.ltwrld.idp.adapter.`in`.web.dto.TokenRequest
import com.ltwrld.idp.adapter.`in`.web.dto.TokenResponse
import com.ltwrld.idp.application.port.`in`.jwt.IssueTokenUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/jwt")
class JwtController(
    private val issueTokenUseCase: IssueTokenUseCase
) {
    @PostMapping("/sign-in")
    fun signIn(@RequestBody request: TokenRequest): TokenResponse {
        val result = issueTokenUseCase.issue(request.userId)

        return TokenResponse(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }
}
package com.ltwrld.idp.domain.model

data class TokenClaim(
    val sub: String,
    val aud: String,
    val iss: String,
    val iat: Long,
    val exp: Long,
    val nonce: String?
)

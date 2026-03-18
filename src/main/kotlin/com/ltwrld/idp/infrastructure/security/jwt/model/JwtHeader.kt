package com.ltwrld.idp.infrastructure.security.jwt.model

data class JwtHeader(
    val alg: String = "RS256",
    val typ: String = "JWT",
    val kid: String
)

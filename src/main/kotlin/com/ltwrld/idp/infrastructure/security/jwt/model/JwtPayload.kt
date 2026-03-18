package com.ltwrld.idp.infrastructure.security.jwt.model

data class JwtPayload(
    val sub: String,
    val aud: String,
    val iss: String,
    val iat: Long,
    val exp: Long,
    val jti: String?
)

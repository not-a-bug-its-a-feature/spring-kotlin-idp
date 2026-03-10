package com.ltwrld.idp.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix="jwt")
data class JwtProperties(
    val issuer: String,
    val audience: String,
    val keyId: String,
    val keys: Map<String, JwtKey>
)

data class JwtKey(
    val privateKey: String,
    val publicKey: String
)
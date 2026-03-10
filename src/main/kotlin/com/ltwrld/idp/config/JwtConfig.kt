package com.ltwrld.idp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.ltwrld.idp.infrastructure.config.JwtProperties
import com.ltwrld.idp.infrastructure.crypto.RsaKeyLoader
import com.ltwrld.idp.infrastructure.security.jwt.Rs256JwtSigner
import com.ltwrld.idp.infrastructure.security.jwt.Rs256JwtVerifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.PublicKey

@Configuration
class JwtConfig(
    private val jwtProperties: JwtProperties,
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun rsaKeyLoader(): RsaKeyLoader {
        return RsaKeyLoader()
    }

    @Bean
    fun jwtSigner(loader: RsaKeyLoader): Rs256JwtSigner {
        val activeKey = jwtProperties.keys[jwtProperties.keyId]
            ?: throw IllegalStateException("Active Key Not Found: Jwt")

        val privateKey = loader.loadPrivateKey(activeKey.privateKey)

        return Rs256JwtSigner(
            privateKey = privateKey,
            objectMapper = objectMapper,
            keyId = jwtProperties.keyId
        )
    }

    @Bean
    fun jwtVerifier(loader: RsaKeyLoader): Rs256JwtVerifier {
        val publicKeys: Map<String, PublicKey> =
            jwtProperties.keys.mapValues { (_, key) ->
                loader.loadPublicKey(key.publicKey)
            }
        return Rs256JwtVerifier(
            publicKeys = publicKeys,
            objectMapper = objectMapper,
            expectedIssuer = jwtProperties.issuer,
            expectedAudience = jwtProperties.audience
        )
    }
}
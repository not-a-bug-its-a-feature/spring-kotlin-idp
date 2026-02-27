package com.ltwrld.idp.infrastructure.crypto

import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Component
class RsaKeyLoader {

    private val keyFactory = KeyFactory.getInstance("RSA")

    fun loadPrivateKey(pem: String): PrivateKey {
        val cleanPem = cleanPem(pem, "PRIVATE KEY")
        val encoded = Base64.getDecoder().decode(cleanPem)
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(encoded))
    }

    fun loadPublicKey(pem: String): PublicKey {
        val cleanPem = cleanPem(pem, "PUBLIC KEY")
        val encoded = Base64.getDecoder().decode(cleanPem)
        return keyFactory.generatePublic(X509EncodedKeySpec(encoded))
    }

    private fun cleanPem(pem: String, type: String): String {
        return pem
            .replace("-----BEGIN $type-----", "")
            .replace("-----END $type-----", "")
            .replace("\\s".toRegex(), "")
    }
}
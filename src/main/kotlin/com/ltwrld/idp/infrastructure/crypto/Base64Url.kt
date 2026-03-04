package com.ltwrld.idp.infrastructure.crypto

import java.util.Base64

object Base64Url {
    fun encode(bytes: ByteArray): String =
        Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)

    fun decode(value: String): ByteArray =
        Base64.getUrlDecoder().decode(value)
}
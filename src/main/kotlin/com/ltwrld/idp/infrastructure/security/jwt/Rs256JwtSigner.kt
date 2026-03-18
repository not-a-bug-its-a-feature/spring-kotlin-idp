package com.ltwrld.idp.infrastructure.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.ltwrld.idp.infrastructure.security.jwt.model.JwtHeader
import com.ltwrld.idp.infrastructure.security.jwt.model.JwtPayload
import com.ltwrld.idp.infrastructure.crypto.Base64Url
import java.security.PrivateKey
import java.security.Signature

class Rs256JwtSigner(
    private val privateKey: PrivateKey,
    private val objectMapper: ObjectMapper,
    private val keyId: String
) {

    fun sign(claim: JwtPayload): String {
        val header = JwtHeader(kid = keyId)

        val headerJson = objectMapper.writeValueAsString(header)
        val payloadJson = objectMapper.writeValueAsString(claim)

        val headerEncoded = Base64Url.encode(headerJson.toByteArray())
        val payloadEncoded = Base64Url.encode(payloadJson.toByteArray())

        val signingInput = "$headerEncoded.$payloadEncoded"

        val signature = Signature.getInstance("SHA256withRSA").apply {
            initSign(privateKey)
            update(signingInput.toByteArray())
        }.sign()

        val signatureEncoded = Base64Url.encode(signature)

        return "$signingInput.$signatureEncoded"
    }

}
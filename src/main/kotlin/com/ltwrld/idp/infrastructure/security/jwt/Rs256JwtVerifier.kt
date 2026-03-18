package com.ltwrld.idp.infrastructure.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.ltwrld.idp.application.port.out.security.TokenVerifier
import com.ltwrld.idp.infrastructure.security.jwt.model.JwtHeader
import com.ltwrld.idp.infrastructure.security.jwt.model.JwtPayload
import com.ltwrld.idp.infrastructure.crypto.Base64Url
import java.security.PublicKey
import java.security.Signature
import java.time.Instant

class Rs256JwtVerifier(
    private val publicKeys: Map<String, PublicKey>,
    private val objectMapper: ObjectMapper,
    private val expectedIssuer: String,
    private val expectedAudience: String,
    private val clockSkewSeconds: Long = 30
) : TokenVerifier {

    override fun verify(token: String) : JwtPayload {
        val parts = token.split(".")

        if (parts.size != 3) throw MalformedJwtException()

        val headerJson = try{
            String(Base64Url.decode(parts[0]))
        } catch (e: Exception) {
            throw MalformedJwtException()
        }
        val header = try {
            objectMapper.readValue(headerJson, JwtHeader::class.java)
        } catch (e: Exception) {
            throw MalformedJwtException()
        }

        if (header.alg != "RS256") {
            throw InvalidAlgorithmException()
        }

        val publicKey = publicKeys[header.kid]
            ?: throw UnknownKeyIdException()

        val signingInput = parts[0] + "." + parts[1]
        val signatureBytes = try {
            Base64Url.decode(parts[2])
        } catch (e: Exception) {
            throw MalformedJwtException()
        }

        val valid = try {
            Signature.getInstance("SHA256withRSA").apply {
                initVerify(publicKey)
                update(signingInput.toByteArray())
            }.verify(signatureBytes)
        } catch (e: Exception) {
            throw InvalidSignatureException()
        }

        if (!valid) throw InvalidSignatureException()

        val payloadJson = try {
            String(Base64Url.decode(parts[1]))
        } catch(e: Exception) {
            throw MalformedJwtException()
        }
        val claim = try {
            objectMapper.readValue(payloadJson, JwtPayload::class.java)
        } catch(e: Exception) {
            throw MalformedJwtException()
        }

        validateClaim(claim)

        return claim

    }

    private fun validateClaim(claim: JwtPayload) {
        val now = Instant.now().epochSecond

        if (claim.iss != expectedIssuer)
            throw InvalidIssuerException()
        if (claim.aud != expectedAudience)
            throw InvalidAudienceException()

        if (now > claim.exp + clockSkewSeconds)
            throw TokenExpiredException()

        if (now + clockSkewSeconds < claim.iat)
            throw InvalidIssuedAtException()
    }
}
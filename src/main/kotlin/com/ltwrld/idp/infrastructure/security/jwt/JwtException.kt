package com.ltwrld.idp.infrastructure.security.jwt

sealed class JwtException(message: String) : RuntimeException(message)

class MalformedJwtException : JwtException("Invalid JWT format")

class InvalidAlgorithmException : JwtException(" Invalid algorithm")

class UnknownKeyIdException : JwtException("Unknown key id")

class InvalidSignatureException : JwtException("Invalid signature")

class InvalidIssuerException : JwtException("Invalid issuer")

class InvalidAudienceException : JwtException("Invalid audience")

class TokenExpiredException : JwtException("Token expired")

class InvalidIssuedAtException : JwtException("Invalid issued-at (iat)")
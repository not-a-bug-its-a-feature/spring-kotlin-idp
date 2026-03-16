package com.ltwrld.idp.application.dto

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
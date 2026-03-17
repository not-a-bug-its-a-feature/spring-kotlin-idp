package com.ltwrld.idp.infrastructure.security.token

import com.ltwrld.idp.application.port.out.security.TokenVerifier
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class TokenAuthenticationWebFilter(
    private val verifiers: List<TokenVerifier>
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        val authHeader = exchange.request.headers.getFirst("Authorization") ?: return chain.filter(exchange)

        val token = authHeader.removePrefix("Bearer ").trim()

        return try {
            verifiers.forEach { it.verify(token) }
            val authentication = UsernamePasswordAuthenticationToken("USER", null, emptyList())

            chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
        } catch (e: Exception) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            exchange.response.setComplete()
        }

    }
}
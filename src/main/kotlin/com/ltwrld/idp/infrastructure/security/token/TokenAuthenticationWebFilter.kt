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
    ): Mono<Void> {

        val token = exchange.request.headers.getFirst("Authorization")
            ?.takeIf {it.startsWith("Bearer ")}
            ?.removePrefix("Bearer ")
            ?.trim()

        if (token == null) {
            return chain.filter(exchange)
        }

        val claim = verifiers
            .asSequence()
            .mapNotNull {
                try {
                    it.verify(token)
                } catch (e: Exception) {
                    //log
                    null
                }
            }
            .firstOrNull() ?: return unauthorized(exchange)

        val auth = UsernamePasswordAuthenticationToken(
            claim.sub,
            token,
            emptyList()
        )
        return chain.filter(exchange)
            .contextWrite(
                ReactiveSecurityContextHolder
                    .withAuthentication(auth)
            )

    }

    private fun unauthorized(exchange: ServerWebExchange): Mono<Void> {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        return exchange.response.setComplete()
    }
}
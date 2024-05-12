package com.github.frtu.coroutine.webclient

import org.springframework.http.HttpStatusCode

/**
 * Response from {@link WebClient}
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
class WebClientResponse(val statusCode: HttpStatusCode, val reponseBody: String?)
package com.github.frtu.coroutine.webclient

import org.springframework.http.HttpStatus

/**
 * Response from {@link WebClient}
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
class WebClientResponse(val statusCode: HttpStatus, val reponseBody: String?)
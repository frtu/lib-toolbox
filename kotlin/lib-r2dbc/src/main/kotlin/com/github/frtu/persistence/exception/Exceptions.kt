package com.github.frtu.persistence.exception

import java.lang.IllegalArgumentException

/**
 * Generic exception to raise Data cannot be fetch from persistence layer
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
class DataNotExist(id: String) : IllegalArgumentException("Id doesn't exist ${id}")
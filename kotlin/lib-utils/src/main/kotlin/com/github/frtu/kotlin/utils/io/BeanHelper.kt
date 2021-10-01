package com.github.frtu.kotlin.utils.io

/**
 * Allow to easily deserialize an object and use object.
 *
 * @author Frédéric TU
 * @since 1.1.4
 */
open class BeanHelper: ResourceHelper() {
    /**
     * Smart cast from List
     */
    fun toStringOrNull(list: List<Any?>, index: Int): String? {
        val result = toStringOrNull(list[index])
        logger.debug("Fetch index:${index} with result:${result}")
        return result
    }

    /**
     * Smart cast from Map
     */
    fun <T> toStringOrNull(map: Map<T, Any?>, key: T): String? {
        val result = toStringOrNull(map[key])
        logger.debug("Fetch key:${key} with result:${result}")
        return result
    }

    /**
     * Smart cast from Any
     */
    fun toStringOrNull(item: Any?): String? =
        if (item is String?) {
            item
        } else {
            null
        }
}
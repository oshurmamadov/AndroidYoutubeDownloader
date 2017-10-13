package com.oshurmamadov.domain.responsehandler

/**
 * Domain layer network request result object
 */
abstract class ResponseHandler<T> {
    var value: T? = null
    var status: Status? = null
    var errorMessage: String? = null
}
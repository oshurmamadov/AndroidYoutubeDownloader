package com.oshurmamadov.data.common

import okhttp3.ResponseBody

/**
 * Data level extension class
 */

fun ResponseBody.convertBodyToString() : String {
    return this.byteStream().bufferedReader().use { it.readText()}
}

fun String.decodeUriComponent(decode: (url: String) -> String) : String {
    return decode(this)
}
package com.oshurmamadov.data.common

import okhttp3.ResponseBody

/**
 * Created by Parviz_Oshurmamadov on 9/13/2017.
 */

fun ResponseBody.convertBodyToStringBuilder() : StringBuilder {
    val stringBuilder = StringBuilder()
    val inputStream = this.byteStream()

    while (inputStream.read() != -1) {
        stringBuilder.append(inputStream.read().toChar())
    }
    return stringBuilder
}
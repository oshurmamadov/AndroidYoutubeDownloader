package com.oshurmamadov.data.common

import android.media.MediaMetadataRetriever
import okhttp3.ResponseBody
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.HashMap

/**
 * Data level extension class
 */

fun ResponseBody.convertBodyToString(): String {
    return this.byteStream().bufferedReader().use { it.readText()}
}

fun String.decodeUriComponent(decode: (url: String) -> String): String {
    return decode(this)
}

fun MediaMetadataRetriever.getVideoDuration(url: String): String {
    var result = ""
    try {
        this.setDataSource(url, HashMap<String, String>())
        result = this.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    }catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}
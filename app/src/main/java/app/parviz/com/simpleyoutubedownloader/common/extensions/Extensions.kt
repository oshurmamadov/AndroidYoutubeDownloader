package app.parviz.com.simpleyoutubedownloader.common.extensions

import android.media.MediaMetadataRetriever

/**
 * Presentations layer extension class
 */

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
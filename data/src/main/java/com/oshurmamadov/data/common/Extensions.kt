package com.oshurmamadov.data.common

import android.media.MediaMetadataRetriever
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.oshurmamadov.domain.util.OSEnvironment
import okhttp3.ResponseBody
import java.io.File

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

fun File.getOutputMediaFile(environment: OSEnvironment): File? {
    val mediaStorageDir = environment.getExternalStoragePublicDirectory()
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null
    }
    return File(mediaStorageDir.path + File.separator + this.path + DEFAULT_APP_VIDEO_FORMAT)
}

fun FFmpeg.trimVideo(trimmingBegin: String, trimmingEnd: String, videoFile: File): String {
    var status = EMPTY
    val newVideoFilePath = videoFile.parent + File.separator + videoFile.name.replace(DEFAULT_APP_VIDEO_FORMAT, EMPTY) +
            DEFAULT_VIDEO_NAME_SUFFIX + DEFAULT_APP_VIDEO_FORMAT
    val cmd = arrayOf(
            FFMPEG_CMD_SS,
            trimmingBegin,
            FFMPEG_CMD_I,
            videoFile.absolutePath,
            FFMPEG_CMD_TO,
            trimmingEnd,
            FFMPEG_CMD_C,
            FFMPEG_CMD_COPY,
            newVideoFilePath)
    try {
        this.execute(cmd, object : ExecuteBinaryResponseHandler() {

            override fun onStart() {}

            override fun onProgress(message: String?) {}

            override fun onFailure(message: String?) {
                status = EMPTY
            }

            override fun onSuccess(message: String?) {
                status = newVideoFilePath
            }

            override fun onFinish() {}
        })
    } catch (e: FFmpegCommandAlreadyRunningException) {
        e.printStackTrace()
        status = EMPTY
    }
    return status
}

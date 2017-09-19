package com.oshurmamadov.data.util

import com.oshurmamadov.data.common.*
import com.oshurmamadov.data.model.VideoInfo
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Low level decoder responsible for decoding incoming stream to {@Link VideoInfoDomainModel}
 */
open class VideoInfoDecoder(private var videoInfo: VideoInfo) {
    private val EMPTY = ""
    private val URL_KEY = "url="
    private val TYPE_KEY = "type="
    private val QUALITY_KEY = "quality="
    private val ENCODING_FORMAT = "UTF-8"

    open fun toDomainModel() : VideoInfoDomainModel {
        val encodedStreamMapUrl = extractEncodedStreamMapUrl(videoInfo.info)
        val decodedStreamMapUrl = decodeURIComponent(encodedStreamMapUrl)

        return createDomainModel(decodedStreamMapUrl)
    }

    private fun extractEncodedStreamMapUrl(source: String?) : String {
        if (source.isNullOrEmpty()) return EMPTY

        val keyArray = source?.split("&")
        val streamMapKey =  keyArray?.find { str: String -> str.contains(URL_ENCODED_FMT_STREAM_MAP_KEY) }

        val mappedUrlArray = streamMapKey?.split("=")

        return if (mappedUrlArray?.size!! >= 2 )
            mappedUrlArray[1]
        else
            EMPTY
    }

    private fun decodeURIComponent(url: String): String {
        if (url.isEmpty()) return EMPTY
        return try {
            URLDecoder.decode(url, ENCODING_FORMAT)
        } catch (exc: UnsupportedEncodingException) {
            exc.printStackTrace()
            return EMPTY
        }
    }

    private fun createDomainModel(decodedMap: String) : VideoInfoDomainModel {
        if (decodedMap.isEmpty()) return VideoInfoDomainModel(true, mutableListOf(), mutableListOf(), mutableListOf())

        val rows = decodedMap.split(",")
        val formedRows = rows.map { str: String ->  str.split("&")}

        val mappedType = formedRows
                .map { array: List<String> ->
                    array.find { str: String ->
                        str.contains(TYPE_KEY)}
                            ?.split("=")?.get(1)
                            ?.decodeUriComponent(this::decodeURIComponent)
                            ?.split(";")?.get(0)
                            ?.split("/")?.get(1)}

        val mappedQuality = convertToCorrectQuality(formedRows
                .map { array: List<String> ->
                    array.find { str: String ->
                        str.contains(QUALITY_KEY)}
                            ?.split("=")?.get(1) })

        val mappedURL =  formedRows
                .map { array: List<String> ->
                    array.find { str: String ->
                        str.contains(URL_KEY)}
                            ?.split("=")?.get(1)
                            ?.decodeUriComponent(this::decodeURIComponent) }

         // TODO get real video content length
        return VideoInfoDomainModel(false, mappedURL.toList(), mappedType.toList(), mappedQuality.toList())
    }

    private fun convertToCorrectQuality(qualityList: List<String?>): List<String?> {
        val newList = qualityList.toTypedArray()

        qualityList.forEachIndexed { index, str ->
            if (str == YOUTUBE_HD)
                newList[index] = APP_HD_KEY
            else if(str == YOUTUBE_MEDIUM && newList.contains(APP_HIGH_KEY))
                newList[index] = APP_MEDIUM_KEY
            else if(str == YOUTUBE_MEDIUM)
                newList[index] = APP_HIGH_KEY
            else if(str == YOUTUBE_SMALL && newList.contains(APP_SMALL_KEY))
                newList[index] = APP_LOW_KEY
            else if(str == YOUTUBE_SMALL)
                newList[index] = APP_SMALL_KEY
        }
        return newList.toList()
    }
}
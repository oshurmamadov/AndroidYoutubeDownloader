package com.oshurmamadov.data.model

import com.oshurmamadov.data.common.URL_ENCODED_FMT_STREAM_MAP_KEY
import com.oshurmamadov.data.common.decodeUriComponent
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Low level model responsible for decoding incoming stream to {@Link VideoInfoDomainModel}
 */
data class VideoInfo(private var info : String = ""){
    private val EMPTY = ""
    private val URL_KEY = "url="
    private val TYPE_KEY = "type="
    private val QUALITY_KEY = "quality="
    private val ENCODING_FORMAT = "UTF-8"

    fun toDomainModel() : VideoInfoDomainModel {
        val encodedStreamMapUrl = extractEncodedStreamMapUrl(info)
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

        val mappedQuality =  formedRows
                .map { array: List<String> ->
                    array.find { str: String ->
                        str.contains(QUALITY_KEY)}
                            ?.split("=")?.get(1)}

        val mappedURL =  formedRows
                .map { array: List<String> ->
                    array.find { str: String ->
                        str.contains(URL_KEY)}
                            ?.split("=")?.get(1)
                            ?.decodeUriComponent(this::decodeURIComponent) }

        return VideoInfoDomainModel(false, mappedURL.toList(), mappedType.toList(), mappedQuality.toList())
    }
}
package com.oshurmamadov.data.model

import com.oshurmamadov.data.common.URL_ENCODED_FMT_STREAM_MAP_KEY
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.ArrayList

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
data class VideoInfo(private var info : String = ""){
    private val TYPE_KEY = "type="
    private val QUALITY_KEY = "quality="

    fun toDomainModel() : VideoInfoDomainModel {
        val encodedStreamMapUrl = extractEncodedStreamMapUrl(info)
        val decodedStreanMapUrl = decodeURIComponent(encodedStreamMapUrl)

        return createDomainModel(decodedStreanMapUrl)
    }

    private fun extractEncodedStreamMapUrl(strBuilder: String) : String {
        val stringArray1 = strBuilder.split("&")
        val stringRes =  stringArray1.find { str: String -> str.contains(URL_ENCODED_FMT_STREAM_MAP_KEY) }
        for (item in stringArray1) {
            if (item.contains(URL_ENCODED_FMT_STREAM_MAP_KEY)) {
                System.out.println("fookin map key :" + item)
            }
        }
        val stringArray2 = stringRes?.split("=")



        return if (stringArray1.size >=2 )
            stringArray1[1]
        else
            ""
    }

    private fun decodeURIComponent(url: String): String {
        if (url.isEmpty()) return ""
        return try {
            URLDecoder.decode(url, "UTF-8")
        } catch (exc: UnsupportedEncodingException) {
            url
        }
    }

    private fun createDomainModel(decodedMap: String) : VideoInfoDomainModel {
        val rows = decodedMap.split(",")
        val formedRows = rows.map { str: String ->  str.split("&")}

        val mappedType = getValueByKey(formedRows, TYPE_KEY)
        val mappedQuality = getValueByKey(formedRows, QUALITY_KEY)

        val mappedResult: List<String>? = null

        return VideoInfoDomainModel("jush","kutoq","kerm")
    }

    private fun getValueByKey(data: List<List<String>>, key: String): ArrayList<String> {
        val mapped = ArrayList<String>()
        for (array in data) {
            for (str in array) {
                if (str.contains(key))
                    if (str.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size >= 2)
                        mapped.add(str.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            }
        }
        return mapped
    }
}
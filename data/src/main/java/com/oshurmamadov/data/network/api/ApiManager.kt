package com.oshurmamadov.data.network.api

import com.oshurmamadov.data.model.VideoInfo
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class ApiManager {
    private val apiClient = ApiClient()

    fun getVideoInfoService(videoID : String) : Call<ResponseBody> {
        return apiClient.getApiService().getVideoInfo(videoID)
    }
}
package com.oshurmamadov.data.network.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
interface InfoService {

    @Streaming
    @GET("/get_video_info")
    fun getVideoInfo(@Query("video_id") videoID: String) : Call<ResponseBody>
}
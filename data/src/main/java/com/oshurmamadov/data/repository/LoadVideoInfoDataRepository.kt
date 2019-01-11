package com.oshurmamadov.data.repository

import com.oshurmamadov.data.common.convertBodyToString
import com.oshurmamadov.data.model.VideoInfo
import com.oshurmamadov.data.network.api.ApiManager
import com.oshurmamadov.data.network.error.APP_ERROR
import com.oshurmamadov.data.network.error.CONNECTION_ERROR
import com.oshurmamadov.data.util.VideoInfoDecoder
import com.oshurmamadov.data.network.error.Result
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
import com.oshurmamadov.domain.responsehandler.Status

/**
 * Data layer video info repository
 */
class LoadVideoInfoDataRepository(private var apiManager: ApiManager): LoadVideoInfoRepository {

    override fun loadVideoInfo(url: String): Result<VideoInfoDomainModel> {
        val responseResult: Result<VideoInfoDomainModel> = Result()

        val response = apiManager.getVideoInfoService(url).execute()

        if (response.isSuccessful) {
            responseResult.status = Status.SUCCESS
            try {
                responseResult.value = VideoInfoDecoder(VideoInfo(response.body().convertBodyToString())).toDomainModel()
            } catch (e: Exception) {
                e.printStackTrace()
                responseResult.status = Status.EXCEPTION
                responseResult.errorMessage = APP_ERROR
            }
        } else {
            responseResult.status = Status.ERROR
            responseResult.errorMessage = CONNECTION_ERROR
        }
        response.body().close()
        return responseResult
    }
}
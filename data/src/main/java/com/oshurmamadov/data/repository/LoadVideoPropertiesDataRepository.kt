package com.oshurmamadov.data.repository

import android.media.MediaMetadataRetriever
import com.oshurmamadov.data.common.getVideoDuration
import com.oshurmamadov.data.network.alternative.OldFashionDownloader
import com.oshurmamadov.data.network.error.Result
import com.oshurmamadov.data.network.error.UNKNOWN_ERROR
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.LoadVideoPropertiesRepository
import com.oshurmamadov.domain.responsehandler.Status

/**
 * Load video duration (in milliseconds) and size repository
 */
class LoadVideoPropertiesDataRepository(mediaMetadataRetriever: MediaMetadataRetriever) : LoadVideoPropertiesRepository {

    override fun loadVideoDuration(videoUrl: String): Result<VideoPropertiesDomainModel> {
        val responseResult: Result<VideoPropertiesDomainModel> = Result()

        val videoSize = OldFashionDownloader().getVideoSize(videoUrl)
        val videoDuration = MediaMetadataRetriever().getVideoDuration(videoUrl)

        if (videoSize == 0 || videoDuration.isEmpty()) {
            responseResult.status = Status.ERROR
            responseResult.errorMessage = UNKNOWN_ERROR
        } else {
            responseResult.status = Status.SUCCESS
            responseResult.value = VideoPropertiesDomainModel(videoDuration, videoSize)
        }

        return responseResult
    }
}
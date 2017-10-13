package com.oshurmamadov.domain.repository

import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.responsehandler.ResponseHandler
import com.oshurmamadov.domain.responsehandler.Status
import io.reactivex.Single

/**
 * Domain layer load video info repository
 */
interface LoadVideoInfoRepository {
    fun loadVideoInfo(url: String) : ResponseHandler<VideoInfoDomainModel>
}
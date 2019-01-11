package com.oshurmamadov.domain.interactor

import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
import com.oshurmamadov.domain.responsehandler.ResponseHandler

/**
 * Load video info use case
 */
class LoadVideoInfoInterActor(private val repository: LoadVideoInfoRepository) : BaseInterActor<ResponseHandler<VideoInfoDomainModel>> {

    private var videoInfoUrl = ""

    override fun setUrl(url: String) {
        videoInfoUrl = url
    }

    override suspend fun buildInterActor(): ResponseHandler<VideoInfoDomainModel> {
        if (videoInfoUrl.isEmpty()) throw IllegalStateException("LoadVideoInfoInterActor: empty url")
        return repository.loadVideoInfo(videoInfoUrl)
    }
}
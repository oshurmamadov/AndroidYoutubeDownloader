package com.oshurmamadov.domain.interactor

import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.repository.LoadVideoPropertiesRepository
import com.oshurmamadov.domain.responsehandler.ResponseHandler

/**
 * Load video duration use case
 */
class LoadVideoPropertiesInterActor(private val repository: LoadVideoPropertiesRepository)
    : BaseInterActor<ResponseHandler<VideoPropertiesDomainModel>> {
    private var videoUrl = ""

    override fun setUrl(url: String) {
        videoUrl = url
    }

    override fun buildInterActor(): ResponseHandler<VideoPropertiesDomainModel> {
        if (videoUrl.isEmpty())
            throw IllegalStateException("LoadVideoDuration: empty url")
        return repository.loadVideoDuration(videoUrl)
    }
}
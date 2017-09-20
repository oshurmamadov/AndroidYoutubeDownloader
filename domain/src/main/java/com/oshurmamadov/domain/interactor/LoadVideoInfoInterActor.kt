package com.oshurmamadov.domain.interactor

import android.util.Log
import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository

/**
 * Load video info use case
 */
class LoadVideoInfoInterActor(private val repository: LoadVideoInfoRepository) : BaseInterActor<VideoInfoDomainModel> {

    private var videoInfoUrl = ""

    override fun setUrl(url: String) {
        videoInfoUrl = url
    }

    override fun buildInterActor(): VideoInfoDomainModel {
        if (videoInfoUrl.isEmpty())
            throw IllegalStateException("LoadVideoInfoInterActor: empty url")
        return repository.loadVideoInfo(videoInfoUrl)
    }
}
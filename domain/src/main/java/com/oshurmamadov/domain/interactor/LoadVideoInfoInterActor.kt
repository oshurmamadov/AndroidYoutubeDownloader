package com.oshurmamadov.domain.interactor

import android.util.Log
import com.oshurmamadov.domain.interactor.common.BaseInterActor
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class LoadVideoInfoInterActor(private val repository: LoadVideoInfoRepository) : BaseInterActor<VideoInfoDomainModel> {

    private var videoInfoUrl = ""

    override fun setUrl(url: String) {
        videoInfoUrl = url
    }

    override fun buildInterActor(): VideoInfoDomainModel {
        if (videoInfoUrl.isEmpty())
            Log.e("LoadVideoInfoInterActor", "empty url")
        return repository.loadVideoInfo(videoInfoUrl)
    }
}
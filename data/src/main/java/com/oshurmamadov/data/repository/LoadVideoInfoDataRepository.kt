package com.oshurmamadov.data.repository

import com.oshurmamadov.data.common.convertBodyToString
import com.oshurmamadov.data.model.VideoInfo
import com.oshurmamadov.data.network.api.ApiManager
import com.oshurmamadov.data.util.VideoInfoDecoder
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository


/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class LoadVideoInfoDataRepository(private var apiManager: ApiManager): LoadVideoInfoRepository {

    override fun loadVideoInfo(url: String): VideoInfoDomainModel {
        return VideoInfoDecoder(
                VideoInfo(apiManager
                            .getVideoInfoService(url)
                            .execute()
                            .body()
                            .convertBodyToString()))
                .toDomainModel()
    }
}
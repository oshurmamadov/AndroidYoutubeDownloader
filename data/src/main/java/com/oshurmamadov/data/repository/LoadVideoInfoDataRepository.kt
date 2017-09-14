package com.oshurmamadov.data.repository

import com.oshurmamadov.data.common.convertBodyToStringBuilder
import com.oshurmamadov.data.model.VideoInfo
import com.oshurmamadov.data.network.alternative.OldFashionDownloader
import com.oshurmamadov.data.network.api.ApiManager
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import com.oshurmamadov.domain.repository.LoadVideoInfoRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class LoadVideoInfoDataRepository(private var apiManager: ApiManager): LoadVideoInfoRepository {

    override fun loadVideoInfo(url: String): Single<VideoInfoDomainModel> {
        return Single.create { emitter: SingleEmitter<VideoInfoDomainModel> ->
            val result = OldFashionDownloader().downloadStreamAndComeWithBuilder(url)
            emitter.onSuccess(VideoInfo(result).toDomainModel())

//            val call = apiManager.getVideoInfoService(url)
//            call.enqueue(object : Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
//                    emitter.onError(t)
//                }
//
//                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
//
//                    emitter.onSuccess(VideoInfo(response.body().convertBodyToStringBuilder()).toDomainModel())
//                }
//            })
        }
    }
}

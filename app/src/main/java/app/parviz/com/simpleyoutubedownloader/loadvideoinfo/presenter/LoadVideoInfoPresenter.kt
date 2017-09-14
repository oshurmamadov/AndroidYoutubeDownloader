package app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter

import android.util.Log
import app.parviz.com.simpleyoutubedownloader.common.base.BasePresenter
import app.parviz.com.simpleyoutubedownloader.common.VIDEO_SIMPLE
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import com.oshurmamadov.domain.interactor.LoadVideoInfoInterActor
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import io.reactivex.observers.DisposableSingleObserver

/**
 * Created by Parviz_Oshurmamadov on 9/6/2017.
 */

class LoadVideoInfoPresenter(private var interActor : LoadVideoInfoInterActor) : BasePresenter<LoadVideoInfoView> {
    private lateinit var view: LoadVideoInfoView

    override fun setView(mView: LoadVideoInfoView) {
        view = mView
    }

    fun loadVideoInfo() {
        view.onLoad()

        interActor.setUrl(VIDEO_SIMPLE)
        interActor.subscribe(VideoInfoObserver(view))
    }

    private class VideoInfoObserver(private var view: LoadVideoInfoView)
        : DisposableSingleObserver<VideoInfoDomainModel>() {

        override fun onSuccess(value: VideoInfoDomainModel) {
            view.hideLoad()
            view.loadVideoInfo(LoadVideoInfoViewModel(value.videoLink, value.videoFormat, value.videoQuality))
        }

        override fun onError(e: Throwable?) {
            Log.e("LoadVideoInfoPresenter", "shit happens")
            view.onError()
        }
    }
}
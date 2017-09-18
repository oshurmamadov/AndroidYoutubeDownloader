package app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter

import app.parviz.com.simpleyoutubedownloader.common.VIDEO_SIMPLE
import app.parviz.com.simpleyoutubedownloader.common.base.BasePresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import com.oshurmamadov.domain.interactor.LoadVideoInfoInterActor
import com.oshurmamadov.domain.model.VideoInfoDomainModel
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Load video info presenter
 */

class LoadVideoInfoPresenter(private var interActor : LoadVideoInfoInterActor) : BasePresenter<LoadVideoInfoView> {
    private lateinit var view: LoadVideoInfoView

    override fun setView(mView: LoadVideoInfoView) {
        view = mView
    }

    fun loadVideoInfo() {
        view.onLoad()

        interActor.setUrl(VIDEO_SIMPLE)
        async(UI) {
            val value = bg { interActor.buildInterActor() }
            proceedWithResult(value.await())
        }
    }

    private fun proceedWithResult(value : VideoInfoDomainModel) {
        view.hideLoad()
        if (value.empty)
            view.onError()
        else
            view.loadVideoInfo(LoadVideoInfoViewModel(value.videoLink, value.videoFormat, value.videoQuality))
    }
}
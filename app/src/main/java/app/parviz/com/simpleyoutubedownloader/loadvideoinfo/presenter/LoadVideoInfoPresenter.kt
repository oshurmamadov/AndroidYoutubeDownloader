package app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter

import app.parviz.com.simpleyoutubedownloader.common.VIDEO_SIMPLE
import app.parviz.com.simpleyoutubedownloader.common.base.BasePresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import app.parviz.com.simpleyoutubedownloader.util.UIThreadCoroutine
import com.oshurmamadov.domain.interactor.LoadVideoInfoInterActor
import com.oshurmamadov.domain.responsehandler.Status
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Load video info presenter
 */

class LoadVideoInfoPresenter(private var infoInterActor : LoadVideoInfoInterActor,
                             private var uiThreadCoroutine: UIThreadCoroutine) : BasePresenter<LoadVideoInfoView> {
    private lateinit var view: LoadVideoInfoView

    override fun setView(mView: LoadVideoInfoView) {
        view = mView
    }

    fun loadVideoInfo() {
        view.onLoad()

        infoInterActor.setUrl(VIDEO_SIMPLE)

        async(uiThreadCoroutine.getUICoroutine()) {
            val value = bg { infoInterActor.buildInterActor() }.await()

            when (value.status) {
                Status.EXCEPTION -> proceedWithError(value.errorMessage)
                Status.ERROR -> proceedWithError(value.errorMessage)
                Status.SUCCESS ->
                    view.loadVideoInfo(LoadVideoInfoViewModel(value.value!!.videoLink, value.value!!.videoFormat, value.value!!.videoQuality))
            }
        }
    }

    private fun proceedWithError(message: String?) {
        view.hideLoad()
        view.onError(message!!)
    }
}
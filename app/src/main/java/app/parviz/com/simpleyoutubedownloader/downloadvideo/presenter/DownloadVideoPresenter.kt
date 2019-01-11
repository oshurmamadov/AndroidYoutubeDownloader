package app.parviz.com.simpleyoutubedownloader.downloadvideo.presenter

import app.parviz.com.simpleyoutubedownloader.common.base.BasePresenter
import app.parviz.com.simpleyoutubedownloader.downloadvideo.view.DownloadVideoView
import app.parviz.com.simpleyoutubedownloader.util.UIThreadCoroutine
import com.oshurmamadov.domain.interactor.DownloadVideoInterActor
import com.oshurmamadov.domain.interactor.LoadVideoPropertiesInterActor
import com.oshurmamadov.domain.model.VideoPropertiesDomainModel
import com.oshurmamadov.domain.responsehandler.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Download video presenter
 */
class DownloadVideoPresenter(private var propertiesInterActor: LoadVideoPropertiesInterActor,
                             private var downloadInterActor: DownloadVideoInterActor,
                             private var uiThreadCoroutine: UIThreadCoroutine): BasePresenter<DownloadVideoView> {

    private lateinit var view: DownloadVideoView

    override fun setView(mView: DownloadVideoView) {
        this.view = mView
    }

    fun downloadVideo(videoUrl: String, videoName: String, trimmingBegin: String, trimmingEnd: String) {
        view.onLoad()
        loadVideoSizeAndDuration(videoUrl, videoName, trimmingBegin, trimmingEnd)
    }

    private fun loadVideoSizeAndDuration(videoUrl : String, videoName: String, trimmingBegin: String, trimmingEnd: String) {
        if (videoUrl.isEmpty())
            proceedWithError()
        else {
            propertiesInterActor.setUrl(videoUrl)

            uiThreadCoroutine.runOnUICoroutine {
                val value = withContext(Dispatchers.IO) {propertiesInterActor.buildInterActor()}

                when (value.status) {
                    Status.ERROR -> proceedWithError(value.errorMessage)
                    Status.SUCCESS -> proceedWithFinResult(videoUrl, videoName, trimmingBegin, trimmingEnd, value.value!!)
                    Status.EXCEPTION -> proceedWithError()
                }
            }
        }
    }

    private fun proceedWithFinResult(videoUrl: String, videoName: String, trimmingBegin: String, trimmingEnd: String,
                                     properties: VideoPropertiesDomainModel) {
        if (properties.size == 0 || properties.duration.isEmpty())
            proceedWithError()
        else {
            downloadInterActor.setUrl(videoUrl)
            downloadInterActor.setMainProperties(videoName, trimmingBegin, trimmingEnd, properties)

            uiThreadCoroutine.runOnUICoroutine {
                val domainModel = withContext(Dispatchers.IO) {downloadInterActor.buildInterActor()}
                if (domainModel.videoFilePath.isEmpty())
                    proceedWithError()
                else {
                    view.hideLoad()
                    view.playVideo(domainModel.videoFilePath)
                }
            }
        }
    }

    private fun proceedWithError(message: String?) {
        if (message.isNullOrEmpty()) proceedWithError()
        view.hideLoad()
        view.onError()
    }

    private fun proceedWithError() {
        view.hideLoad()
        view.onError()
    }
}
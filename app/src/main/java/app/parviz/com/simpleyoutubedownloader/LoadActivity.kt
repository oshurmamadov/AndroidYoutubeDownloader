package app.parviz.com.simpleyoutubedownloader

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import app.parviz.com.simpleyoutubedownloader.common.App
import app.parviz.com.simpleyoutubedownloader.common.VIDEO_SIMPLE
import app.parviz.com.simpleyoutubedownloader.common.base.BaseActivity
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import com.oshurmamadov.data.common.VIDEO_INFO_URL
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.BufferedInputStream
import java.net.URL
import javax.inject.Inject

class LoadActivity : BaseActivity(), LoadVideoInfoView  {
    @Inject
    lateinit var loadVideoInfoPresenter: LoadVideoInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        initDiGraph()
        initPresenter()

        findViewById(R.id.load_stream).setOnClickListener {
            getVideoInfo()
        }
    }

    override fun onLoad() {
        Log.d("LoadActivity", "loading video info...")
    }

    override fun onError() {
        Log.e("LoadActivity", "what da foook ? sgeyn !")
    }

    override fun loadVideoInfo(viewModel: LoadVideoInfoViewModel) {
        Log.d("LoadActivity", "link : " + viewModel.videoLink)
        Log.d("LoadActivity", "format : " + viewModel.videoFormat)
        Log.d("LoadActivity", "quality : " + viewModel.videoQuality)
    }

    override fun hideLoad() {
        Log.d("LoadActivity", "loading video info...")
    }

    private fun initDiGraph() {
        App.graph.inject(this)
    }
    private fun initPresenter() {
        loadVideoInfoPresenter.setView(this)
    }
    private fun getVideoInfo() {
        loadVideoInfoPresenter.loadVideoInfo()
    }
}
package app.parviz.com.simpleyoutubedownloader

import android.os.Bundle
import android.util.Log
import app.parviz.com.simpleyoutubedownloader.common.App
import app.parviz.com.simpleyoutubedownloader.common.base.BaseActivity
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import javax.inject.Inject

class LoadActivity : BaseActivity(), LoadVideoInfoView  {
    @Inject
    lateinit var loadVideoInfoPresenter: LoadVideoInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        initDiGraph()
        initPresenter()

        getVideoInfo()
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
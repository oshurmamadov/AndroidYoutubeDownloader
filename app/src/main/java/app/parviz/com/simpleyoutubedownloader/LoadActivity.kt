package app.parviz.com.simpleyoutubedownloader

import android.os.Bundle
import android.support.v7.widget.AppCompatSpinner
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import app.parviz.com.simpleyoutubedownloader.common.base.BaseActivity
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_load.*

class LoadActivity : BaseActivity(), LoadVideoInfoView  {
    @Inject
    lateinit var loadVideoInfoPresenter: LoadVideoInfoPresenter

    private val loadVideoButton: Button by lazy { load_video }
    private val loadStreamButton: Button by lazy { load_stream }
    private val videoLoadWrapper: LinearLayout by lazy { video_load_wrapper }
    private val videoQualitySpinner: AppCompatSpinner by lazy { video_quality_spinner }

    private var mViewModel: LoadVideoInfoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        initDiGraph()
        initPresenter()

        loadStreamButton.setOnClickListener {
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
        populateVideoQualitySpinner(viewModel)
        mViewModel = viewModel
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

    private fun populateVideoQualitySpinner(viewModel: LoadVideoInfoViewModel) {
        videoLoadWrapper.visibility = View.VISIBLE

        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, viewModel.videoQuality)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        videoQualitySpinner.adapter = adapter

        videoQualitySpinner.setSelection(viewModel.videoQuality.size - 1)
    }
}
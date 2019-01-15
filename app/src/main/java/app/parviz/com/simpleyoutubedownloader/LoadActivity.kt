package app.parviz.com.simpleyoutubedownloader

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.AppCompatSpinner
import android.util.Log
import android.view.View
import android.widget.*
import app.parviz.com.simpleyoutubedownloader.common.READ_AND_WRITE_PERMISSION_REQUEST_CODE
import app.parviz.com.simpleyoutubedownloader.common.base.BaseActivity
import app.parviz.com.simpleyoutubedownloader.downloadvideo.presenter.DownloadVideoPresenter
import app.parviz.com.simpleyoutubedownloader.downloadvideo.view.DownloadVideoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel
import app.parviz.com.simpleyoutubedownloader.player.CustomYouTubePlayerFragment
import app.parviz.com.simpleyoutubedownloader.player.CustomYoutubePlayerListener
import kotlinx.android.synthetic.main.activity_load.*
import javax.inject.Inject

class LoadActivity : BaseActivity() {

    @Inject lateinit var loadVideoInfoPresenter: LoadVideoInfoPresenter
    @Inject lateinit var downloadVideoPresenter: DownloadVideoPresenter

    private val loadVideoButton: Button by lazy { load_video }
    private val loadInfoButton: Button by lazy { load_info }
    private val videoLoadWrapper: LinearLayout by lazy { video_load_wrapper }
    private val videoQualitySpinner: AppCompatSpinner by lazy { video_quality_spinner }

    private var globalVideoUrl = ""
    private lateinit var mViewModel: LoadVideoInfoViewModel
    private var customYoutubePlayerListener: CustomYoutubePlayerListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        if (!requestPermissions(this))
            initLoadingListeners()

        initDiGraph()
        initPresenters()
        initYoutubePlayer()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            READ_AND_WRITE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    initLoadingListeners()
                else
                //TODO Add proper this kinda error handling mechanism
                    Toast.makeText(applicationContext, "Oh dear we can not proceed without this permissions ;( ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setCustomYoutubePlayerListener(listener: CustomYoutubePlayerListener) {
        this.customYoutubePlayerListener = listener
    }

    private fun initDiGraph() {
        App.graph.inject(this)
    }

    private fun initPresenters() {
        loadVideoInfoPresenter.setView(LoadVideoInfoViewImpl())
        downloadVideoPresenter.setView(DownloadVideoViewImpl())
    }

    private fun initLoadingListeners() {
        loadInfoButton.setOnClickListener { getVideoInfo() }
        loadVideoButton.setOnClickListener { downloadVideo() }
    }

    private fun initYoutubePlayer() {
        supportFragmentManager.beginTransaction().add(R.id.player_container, CustomYouTubePlayerFragment()).commit()
    }

    private fun getVideoInfo() {
        loadVideoInfoPresenter.loadVideoInfo()
    }

    private fun downloadVideo() {
        // TODO why trimming begin and end are NOT Integers, a ? gathat nakchugjo markab nakhuy, sgeyn bleat ?
        downloadVideoPresenter.downloadVideo(globalVideoUrl, "testName", "3000", "7000")
    }

    private fun populateVideoQualitySpinner(viewModel: LoadVideoInfoViewModel) {
        videoLoadWrapper.visibility = View.VISIBLE

        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, viewModel.videoQuality)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        videoQualitySpinner.adapter = adapter

        videoQualitySpinner.setSelection(viewModel.videoQuality.size - 1)

        videoQualitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                globalVideoUrl = mViewModel.videoLink[i]!!
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
            }
        }
    }

    private fun initPlayerAndPlayVideo(videoPath: String) {
        checkNotNull(customYoutubePlayerListener)
        customYoutubePlayerListener!!.playVideo(videoPath)
    }

    private inner class LoadVideoInfoViewImpl: LoadVideoInfoView {

        override fun onError(message: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onLoad() {
            Log.d("LoadActivity", "loading video info...")
        }

        override fun hideLoad() {
            Log.d("LoadActivity", "hide video info loading")
        }

        override fun onError() {
            Log.e("LoadActivity", "loading video info ERROR")
        }

        override fun loadVideoInfo(viewModel: LoadVideoInfoViewModel) {
            Log.d("LoadActivity", "link : " + viewModel.videoLink)
            Log.d("LoadActivity", "format : " + viewModel.videoFormat)
            Log.d("LoadActivity", "quality : " + viewModel.videoQuality)
            populateVideoQualitySpinner(viewModel)
            mViewModel = viewModel
        }
    }

    private inner class DownloadVideoViewImpl: DownloadVideoView {

        override fun onError(message: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun hideLoad() {
            Log.d("LoadActivity", "hide video stream loading")
        }

        override fun onError() {
            Log.e("LoadActivity", "loading video stream ERROR")
        }

        override fun onLoad() {
            Log.e("LoadActivity", "loading video stream...")
        }

        override fun playVideo(videoPath: String) {
            initPlayerAndPlayVideo(videoPath)
        }
    }
}
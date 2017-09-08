package app.parviz.com.simpleyoutubedownloader

import android.os.Bundle
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.presenter.LoadVideoInfoPresenter
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view.LoadVideoInfoView
import javax.inject.Inject

class LoadActivity : BaseActivity(), LoadVideoInfoView  {

    @Inject
    lateinit var loadVideoInfoPresenter: LoadVideoInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        App.graph.inject(this)

    }

    override fun onLoad() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadVideoInfo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

package app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view

import app.parviz.com.simpleyoutubedownloader.common.base.BaseStateView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel

/**
 * Created by Parviz_Oshurmamadov on 9/6/2017.
 */
interface LoadVideoInfoView : BaseStateView {
    fun loadVideoInfo(viewModel: LoadVideoInfoViewModel)
}
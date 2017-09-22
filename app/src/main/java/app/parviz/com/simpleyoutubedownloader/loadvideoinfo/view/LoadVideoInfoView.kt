package app.parviz.com.simpleyoutubedownloader.loadvideoinfo.view

import app.parviz.com.simpleyoutubedownloader.common.base.BaseStateView
import app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel.LoadVideoInfoViewModel

/**
 * Load video info view
 */
interface LoadVideoInfoView : BaseStateView {
    fun loadVideoInfo(viewModel: LoadVideoInfoViewModel)
}
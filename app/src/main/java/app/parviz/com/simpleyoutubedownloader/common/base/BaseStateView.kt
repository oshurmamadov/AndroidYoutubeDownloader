package app.parviz.com.simpleyoutubedownloader.common.base

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
interface BaseStateView {
    fun onLoad()
    fun hideLoad()
    fun onError()
    fun onError(message: String)
}
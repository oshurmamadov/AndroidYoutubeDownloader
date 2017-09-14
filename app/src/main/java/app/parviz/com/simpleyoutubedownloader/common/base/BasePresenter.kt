package app.parviz.com.simpleyoutubedownloader.common.base

/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
interface BasePresenter<in T> {
    fun setView(mView: T)
}
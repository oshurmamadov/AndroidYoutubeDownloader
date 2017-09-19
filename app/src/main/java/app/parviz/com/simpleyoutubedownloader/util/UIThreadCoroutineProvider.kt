package app.parviz.com.simpleyoutubedownloader.util

import kotlinx.coroutines.experimental.android.HandlerContext
import kotlinx.coroutines.experimental.android.UI

/**
 * UI thread coroutine provider.
 */
class UIThreadCoroutineProvider: UIThreadCoroutine {
    override fun getUICoroutine(): HandlerContext {
        return UI
    }
}
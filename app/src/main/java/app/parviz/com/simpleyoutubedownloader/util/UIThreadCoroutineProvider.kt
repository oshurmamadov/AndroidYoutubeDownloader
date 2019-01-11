package app.parviz.com.simpleyoutubedownloader.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * UI thread coroutine provider.
 */
class UIThreadCoroutineProvider: UIThreadCoroutine {

    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    override fun <T> runOnUICoroutine(runnable: suspend () -> T) {
        uiScope.launch { runnable() }
    }
}
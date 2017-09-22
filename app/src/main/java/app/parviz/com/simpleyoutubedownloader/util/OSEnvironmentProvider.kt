package app.parviz.com.simpleyoutubedownloader.util

import android.os.Environment
import app.parviz.com.simpleyoutubedownloader.common.APP_DIR_NAME
import com.oshurmamadov.domain.util.OSEnvironment
import java.io.File

/**
 * Android OS environment storage provider
 */
class OSEnvironmentProvider: OSEnvironment {
    override fun getExternalStoragePublicDirectory(): File {
        return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), APP_DIR_NAME)
    }
}
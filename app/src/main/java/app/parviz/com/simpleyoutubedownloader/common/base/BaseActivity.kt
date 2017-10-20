package app.parviz.com.simpleyoutubedownloader.common.base

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import app.parviz.com.simpleyoutubedownloader.common.READ_AND_WRITE_PERMISSION_REQUEST_CODE

/**
 * Base Activity
 */
abstract class BaseActivity : AppCompatActivity() {

    fun requestPermissions(activity: Activity): Boolean {
        return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_AND_WRITE_PERMISSION_REQUEST_CODE)
            true
        } else
            false
    }
}
package com.oshurmamadov.domain.util

import java.io.File

/**
 * Android OS Environment provider
 */
interface OSEnvironment {
    fun getExternalStoragePublicDirectory(): File
}
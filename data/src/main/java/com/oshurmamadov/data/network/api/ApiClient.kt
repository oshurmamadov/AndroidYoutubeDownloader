package com.oshurmamadov.data.network.api

import com.google.gson.GsonBuilder
import com.oshurmamadov.data.common.ROOT_URL
import com.oshurmamadov.data.network.service.InfoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class ApiClient {
    private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    private val retrofit = Retrofit
            .Builder()
            .baseUrl(ROOT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build()

    private fun <T> createApiService(service: Class<T>) : T {
        return retrofit.create(service)
    }

    fun getApiService() : InfoService {
        return createApiService(InfoService::class.java)
    }
}
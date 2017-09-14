package com.oshurmamadov.data.network.api

import com.google.gson.GsonBuilder
import com.oshurmamadov.data.common.ROOT_URL
import com.oshurmamadov.data.network.service.InfoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by Parviz_Oshurmamadov on 9/11/2017.
 */
class ApiClient {
    private val retrofit: Retrofit = Retrofit
            .Builder()
            .baseUrl(ROOT_URL)
            .addConverterFactory(
                    GsonConverterFactory.create(
                            GsonBuilder().setLenient().create()))
            .client(
                    OkHttpClient.Builder().addInterceptor(
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
            .build()

    private fun <T> createApiService(service: Class<T>) : T {
        return retrofit.create(service)
    }

    fun getApiService() : InfoService {
        return createApiService(InfoService::class.java)
    }
}
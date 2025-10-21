package com.example.productlisttestapp.apiutils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BaseUrl = "https://api-new.fmb.eposapi.co.uk/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApi(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}

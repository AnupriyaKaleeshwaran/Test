package com.example.productlisttestapp.apiutils

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {


    companion object {
        private const val BaseUrl = "https://reqres.in/"
        private const val API_KEY = "reqres-free-v1"

        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {
            if (retrofit != null) {
                return retrofit
            }


            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


            val headerInterceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-API-Key", API_KEY)
                    .build()
                chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val gson = GsonBuilder().setLenient().create()

            retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            return retrofit
        }

        fun getApiInterface(): ApiInterface? {
            return getClient()?.create(ApiInterface::class.java)
        }
    }
}




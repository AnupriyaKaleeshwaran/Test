package com.example.productlisttestapp.apiutils

import com.example.productlisttestapp.model.ApiResponse
import okhttp3.RequestBody

import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("getOrderByTableId")
    @Headers("Content-Type: text/plain")
    suspend fun getOrderByTableId(@Body body: RequestBody): Response<ApiResponse>
}
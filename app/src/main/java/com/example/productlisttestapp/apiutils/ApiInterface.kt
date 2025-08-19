package com.example.productlisttestapp.apiutils

import com.example.productlisttestapp.model.LoginRequest
import com.example.productlisttestapp.model.LoginResponse
import com.example.productlisttestapp.model.UserListResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("api/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("api/users")
    suspend fun getUsers(@Query("page") page: Int = 1): UserListResponse
}
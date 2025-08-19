package com.example.productlisttestapp.repository

import androidx.lifecycle.LiveData
import com.example.productlisttestapp.apiutils.ApiClient
import com.example.productlisttestapp.db.UserDao
import com.example.productlisttestapp.db.UserEntity
import com.example.productlisttestapp.model.LoginRequest
import com.example.productlisttestapp.model.LoginResponse

class UserRepository(private val userDao: UserDao) {
    val api = ApiClient.getApiInterface()


    fun getUsers(): LiveData<List<UserEntity>> = userDao.selectAll()


    suspend fun refreshUsers() {
        val api = ApiClient.getApiInterface()
        val response = api?.getUsers(1)
        response?.data?.let { list ->
            userDao.insertAll(list.map {
                UserEntity(
                    it.id,
                    it.firstName,
                    it.lastName,
                    it.email,
                    it.avatar
                )
            })
        }
    }

    fun searchUsers(query: String): LiveData<List<UserEntity>> {
        //  Log.e("query", query)
        return userDao.search("%$query%")
    }

    suspend fun login(email: String, password: String): LoginResponse? {
        // Log.e("email", email)
        //  Log.e("password", password)
        return api?.login(LoginRequest(email, password))
    }
}


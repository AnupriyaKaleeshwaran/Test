package com.example.productlisttestapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productlisttestapp.db.UserEntity
import com.example.productlisttestapp.model.User
import com.example.productlisttestapp.repository.UserRepository
import kotlinx.coroutines.launch



class UsersViewModel(private val repository: UserRepository) : ViewModel() {

    val users: LiveData<List<UserEntity>> = repository.getUsers()


    fun refreshUsers() {
        viewModelScope.launch {
            try {
                repository.refreshUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun search(query: String): LiveData<List<UserEntity>> {
        return repository.searchUsers(query)
    }
}

package com.example.productlisttestapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun selectAll(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users WHERE firstName LIKE :q OR lastName LIKE :q OR email LIKE :q")
    fun search(q: String): LiveData<List<UserEntity>>
}

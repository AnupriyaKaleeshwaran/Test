package com.example.productlisttestapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productlisttestapp.adapter.UserAdapter
import com.example.productlisttestapp.databinding.ActivityMainBinding
import com.example.productlisttestapp.db.AppDatabase
import com.example.productlisttestapp.db.UserDao
import com.example.productlisttestapp.repository.UserRepository
import com.example.productlisttestapp.viewmodel.UsersViewModel

class UserListActivity : AppCompatActivity() {

    private lateinit var userModel: UsersViewModel
    private lateinit var repository: UserRepository
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getInstance(this).userDao()
        repository = UserRepository(userDao)
        userModel = UsersViewModel(repository)


        setAdapter()

        userModel.refreshUsers()

        userModel.users.observe(this) { users ->
            adapter.setUsers(users)
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                val q = newText ?: ""
                userModel.search(q).observe(this@UserListActivity) { list ->
                    adapter.setUsers(list)
                }
                return true
            }
        })
    }

    private fun setAdapter() {
        adapter = UserAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}



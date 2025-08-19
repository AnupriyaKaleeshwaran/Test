package com.example.productlisttestapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.productlisttestapp.apiutils.SessionManager
import com.example.productlisttestapp.databinding.ActivityLoginBinding
import com.example.productlisttestapp.db.AppDatabase
import com.example.productlisttestapp.db.UserDao
import com.example.productlisttestapp.repository.UserRepository
import com.example.productlisttestapp.viewmodel.LoginViewModel
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var session: SessionManager
    private lateinit var repository: UserRepository
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getInstance(this).userDao()
        repository = UserRepository(userDao)
        viewModel = LoginViewModel(repository)
        session = SessionManager(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                session.setLogin(binding.etEmail.text.toString())
                startActivity(Intent(this, UserListActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



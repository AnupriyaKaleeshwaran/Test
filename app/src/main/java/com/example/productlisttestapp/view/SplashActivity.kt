package com.example.productlisttestapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import com.example.productlisttestapp.apiutils.SessionManager
import com.example.productlisttestapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(applicationContext)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadingSpinner.visibility = ProgressBar.VISIBLE

        Handler().postDelayed({
            binding.loadingSpinner.visibility = ProgressBar.GONE
            navigate()
        }, 2500)
    }

    private fun navigate() {
        if (session.isLogin()) {
            navigateToMainActivity()
        } else {
            navigateToLoginActivity()
        }
    }


    private fun navigateToMainActivity() {
        startActivity(Intent(this, UserListActivity::class.java))
        finish()
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
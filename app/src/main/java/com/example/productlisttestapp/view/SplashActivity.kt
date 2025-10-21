package com.example.productlisttestapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import com.example.productlisttestapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadingSpinner.visibility = ProgressBar.VISIBLE

        Handler().postDelayed({
            binding.loadingSpinner.visibility = ProgressBar.GONE
            navigate()
        }, 2500)
    }

    private fun navigate() {
        navigateToMainActivity()
    }


    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
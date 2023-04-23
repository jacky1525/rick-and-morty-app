package com.jacky.rickandmorty.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.jacky.rickandmorty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    var runnable: Runnable = Runnable {}
    var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences =
            this.getSharedPreferences("com.jacky.rickandmorty", Context.MODE_PRIVATE)

        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        runnable = Runnable {
            val intent = Intent(this@MainActivity, HomePage::class.java)
            startActivity(intent)
            finish()
        }
        handler.postDelayed(runnable, 3000)

        if (isFirstTime) {
            binding.textView.text = "Welcome!"

            sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
        } else {
            binding.textView.text = "Hello!"
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
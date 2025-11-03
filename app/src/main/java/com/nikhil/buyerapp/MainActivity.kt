package com.nikhil.buyerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nikhil.buyerapp.Signup.SignupActivity
import com.nikhil.buyerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvtitlee.visibility = View.INVISIBLE
        binding.tvtitlee2.visibility = View.INVISIBLE


        binding.lottiee.setAnimation(R.raw.animaa)
        binding.lottiee.repeatCount = 0
        binding.lottiee.speed = 2.0f


        binding.lottiee.addLottieOnCompositionLoadedListener { composition ->
            binding.lottiee.playAnimation()


            binding.tvtitlee.visibility = View.VISIBLE
            binding.tvtitlee2.visibility = View.VISIBLE
            binding.tvtitlee.alpha = 0f
            binding.tvtitlee2.alpha = 0f
            binding.tvtitlee.animate().alpha(1f).setDuration(300).start()
            binding.tvtitlee2.animate().alpha(1f).setDuration(500).start()

            val duration = (composition?.duration ?: 3000L).toFloat()/(binding.lottiee.speed)

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, SignupActivity::class.java))
                finish()
            }, duration.toLong())
        }
    }
}
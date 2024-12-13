package com.example.soundmood.ui.gettingstartedpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.soundmood.R
import com.example.soundmood.databinding.ActivityGettingStartedBinding
import com.example.soundmood.ui.loginpage.LoginActivity

class GettingStartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGettingStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGettingStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        binding.imageView.startAnimation(slideDown)
        slideDown.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // Mulai animasi fade-in setelah slide-down selesai
                binding.tvTagline.startAnimation(fadeIn)
                binding.soundwave.startAnimation(fadeIn)
                binding.btnGenerateplaylist.startAnimation(fadeIn)

                // Pastikan elemen terlihat
                binding.tvTagline.visibility = ImageView.VISIBLE
                binding.soundwave.visibility = ImageView.VISIBLE
                binding.btnGenerateplaylist.visibility = ImageView.VISIBLE

                binding.tvTagline.alpha = 1f
                binding.soundwave.alpha = 1f
                binding.btnGenerateplaylist.alpha = 1f
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })

        binding.btnGenerateplaylist.setOnClickListener {
            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            // Mulai animasi fade-out dan slide-up
            binding.imageView.startAnimation(slideUp)
            binding.tvTagline.startAnimation(fadeOut)
            binding.soundwave.startAnimation(fadeOut)
            binding.btnGenerateplaylist.startAnimation(fadeOut)

            fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}

                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    // Sembunyikan elemen agar tidak terlihat kembali
                    binding.imageView.visibility = View.GONE
                    binding.tvTagline.visibility = View.GONE
                    binding.soundwave.visibility = View.GONE
                    binding.btnGenerateplaylist.visibility = View.GONE

                    // Pindah ke LoginActivity setelah animasi selesai
                    val intent = Intent(this@GettingStartedActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Menutup activity ini
                }

                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            })
        }
    }
}

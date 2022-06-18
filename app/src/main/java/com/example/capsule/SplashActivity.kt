package com.example.capsule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val leftCapsuleImage: ImageView = findViewById<ImageView>(R.id.splash_left)
        val leftAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_capsule_left)
        leftCapsuleImage.startAnimation(leftAnimation)

        val rightCapsuleImage: ImageView = findViewById<ImageView>(R.id.splash_right)
        val rightAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_capsule_right)
        rightCapsuleImage.startAnimation(rightAnimation)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}

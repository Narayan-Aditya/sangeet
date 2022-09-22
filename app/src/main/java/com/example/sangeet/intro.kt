package com.example.sangeet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_intro.*
@Suppress("DEPRECATION")
class intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        img.animate().translationX(-2000F).setDuration(3000).startDelay = 2000;
        name.animate().translationY(-2000F).setDuration(1000).startDelay = 2000;
        lotti.animate().translationY(2000F).setDuration(1000).startDelay = 2000;

        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java));
            finish();
        },2500)
    }
}
package com.example.gydealpha.startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.gydealpha.R
import com.example.gydealpha.profileBuilder.ProfileBuilderActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 1000 // 1/3 of sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)    //displays XML layout

        // This code will be executed once the timer is over
        Handler().postDelayed({
            //startActivity<ProfileBuilderActivity>()
            startActivity<SignInActivity>()     //navigate to next Activity
            finish()    //end this activity

        }, SPLASH_TIME_OUT)
    }

}



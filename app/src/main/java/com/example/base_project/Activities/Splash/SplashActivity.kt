package com.example.base_project.Activities.Splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.base_project.Activities.Home.createHomeIntent
import com.example.base_project.Activities.Login.createLoginIntent
import com.example.base_project.Utils.BaseActivity
import com.example.base_project.Utils.PreferencesHelper
import com.example.base_project.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        PreferencesHelper.init(this)

        gotoLogin()
    }

    private fun gotoLogin(){
        Handler().postDelayed({
            if (PreferencesHelper.session != null) {
                startActivity(createHomeIntent(true))
            }
            else{
                startActivity(createLoginIntent())
            }
        }, 1000)
    }
}
package com.example.employeeandproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.employeeandproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashScreenActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=Firebase.auth
        setContentView(R.layout.activity_splash_screen)

        val currentUser=auth.currentUser
        Thread({
            kotlin.run {
                Thread.sleep(3000)
                if (currentUser!=null){
                    startActivity(Intent(this,DashBoardActivity::class.java))
                }else{
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }).start()

    }
}
package com.example.employeeandproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.employeeandproject.databinding.ActivityLoginBinding
import com.example.loginregisterwithmysql.Utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth

        binding.btnLogin.setOnClickListener {

            var email = binding.etEmail.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()

            if (!Utils.isValidEmail(email)) {
                binding.inputEmail.error = "Enter Valid Email"
            } else if (!Utils.isValidPassword(password)) {
                binding.inputPassword.error = "Enter Valid Password"
            } else {
                userLogin(email,password)
            }
        }
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }


    }
    private fun userLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                var user = auth.currentUser
                Toast.makeText(this, """
                    id : ${user?.uid}
                    email : ${user!!.email}
                """.trimIndent(), Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, DashBoardActivity::class.java))
                finishAffinity()

            }else{
                Toast.makeText(this, "${it.exception.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }




}

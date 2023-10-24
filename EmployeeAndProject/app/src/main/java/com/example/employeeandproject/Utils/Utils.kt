package com.example.loginregisterwithmysql.Utils

import android.util.Patterns
import java.util.regex.Pattern

class Utils {

    companion object{

        fun isValidEmail(email:String):Boolean{

           return Patterns.EMAIL_ADDRESS.matcher(email).matches()

        }

        fun isValidPassword(password:String):Boolean{
            // Regex to check valid password.
            val regex = ("^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$")
            return Pattern.matches(regex,password)

        }

    }
}
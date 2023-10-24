package com.example.employeeandproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.employeeandproject.databinding.ActivitySignupBinding
import com.example.employeeandproject.models.EmployeeDetail
import com.example.loginregisterwithmysql.Utils.Utils
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database

import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    private lateinit var mRef: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.database
        mRef = db.reference



        binding.btnSignup.setOnClickListener {
            var fName = binding.etFname.text.toString().trim()
            var lName = binding.etLname.text.toString().trim()
            var email = binding.etEmail.text.toString().trim()
            var contact = binding.etContact.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()
            var cpassword = binding.etCpassword.text.toString().trim()

            resetError()

            if (fName.isEmpty()) {
                showError(binding.inputFname, "First Name")
            } else if (lName.isEmpty()) {
                showError(binding.inputLname, "First Name")
            } else if (!Utils.isValidEmail(email)) {
                showError(binding.inputEmail, "Enter Valid Email")
            } else if (contact.length != 10) {
                showError(binding.inputContact, "Enter Valid Contact")
            } else if (!Utils.isValidPassword(password)) {
                showError(
                    binding.inputPassword,
                    "Password with 1 Capital,1 special chracter and lenght 8"
                )
            } else if (cpassword != password) {
                showError(binding.inputCpassword, "Password Mismatch")
            } else
                createAccount(fName, lName, email, password, contact)

        }
    }

    private fun createAccount(
        fName: String,
        lName: String,
        email: String,
        password: String,
        contact: String
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                var user = auth.currentUser

                user?.let {
                    var mUser = EmployeeDetail(fname = fName, lname = lName, email = email, password = password, contact = contact, id = auth.currentUser!!.uid)
                    mRef.child("EMPLOYEE DATA NODE").child(auth.currentUser!!.uid).setValue(mUser).addOnSuccessListener {
                        Toast.makeText(this, "${mUser.fname} ${mUser.lname} Your account created", Toast.LENGTH_SHORT).show()
                        startActivity( Intent(this, DashBoardActivity::class.java))
                    }.addOnFailureListener {
                        Toast.makeText(this, "Sorry try again later", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "${it.exception.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun resetError() {
        binding.inputFname.error = null
        binding.inputLname.error = null
        binding.inputEmail.error = null
        binding.inputPassword.error = null
        binding.inputCpassword.error = null
    }

    private fun showError(inputLayout: TextInputLayout, message: String) {
        inputLayout.error = message
        inputLayout.requestFocus()

    }


}
package com.example.employeeandproject.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.employeeandproject.R
import com.example.employeeandproject.databinding.ActivityManageProfileBinding
import com.example.employeeandproject.models.EmployeeDetail
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class ManageProfileActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var mRef: DatabaseReference
    private lateinit var db: FirebaseDatabase
    lateinit var binding: ActivityManageProfileBinding
    private var imageUri: Uri? = null
    var storageRef = Firebase.storage.reference
    private var existingUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.database
        mRef = Firebase.database.reference
        auth = Firebase.auth

        imageUri = createImageUri()
        val cameraResultLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) {
                if (it) {
                    binding.ivThumbnail.setImageURI(imageUri)
                } else {
                    imageUri = null
                }
            }

        binding.ivThumbnail.setOnClickListener {
            cameraResultLauncher.launch(imageUri)
        }




        mRef.child("EMPLOYEE DATA NODE").child(auth.currentUser!!.uid)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var data = snapshot.getValue(EmployeeDetail::class.java)
                    if (data != null) {

                        binding.etFname.setText(data.fname)
                        binding.etLname.setText(data.lname)
                        binding.etEmail.setText(data.email)
                        binding.etContact.setText(data.contact)
                        binding.etPassword.setText(data.password)

                        Glide
                            .with(applicationContext)
                            .load(data.profileUrl)
                            .centerCrop()
                            .placeholder(R.drawable.baseline_broken_image_24)
                            .into(binding.ivThumbnail)

                        existingUrl=data.profileUrl



                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //TODO("Not yet implemented")
                }
            })



        binding.btnUpdate.setOnClickListener {

            var fName = binding.etFname.text.toString().trim()
            var lName = binding.etLname.text.toString().trim()
            var email = binding.etEmail.text.toString().trim()
            var contact = binding.etContact.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()

            updateAccount(fName, lName, email, password, contact, imageUri)


        }


    }

    private fun createImageUri(): Uri {

        var fileName = "${System.currentTimeMillis()}.png"
        var file = File(filesDir, fileName)
        return FileProvider.getUriForFile(this, "com.example.employeeandproject.fileProvide", file)

    }

    private fun updateAccount(
        fName: String,
        lName: String,
        email: String,
        password: String,
        contact: String,
        imageUri: Uri?
    ) {
        getDownLoadUrl(imageUri) { downloadUrl ->

            if (downloadUrl != null)

                auth.currentUser!!.uid.let {
                    var employeeDetail = EmployeeDetail(
                        fname = fName,
                        lname = lName,
                        email = email,
                        contact = contact,
                        password = password,
                        id = it,
                        profileUrl = downloadUrl
                    )
                    mRef.child("EMPLOYEE DATA NODE").child(it).setValue(employeeDetail)
                        .addOnSuccessListener {

                            Toast.makeText(this, "Detail updated", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show()
                        }

                }
        }
    }

    private fun getDownLoadUrl(imageUri: Uri?, onComplete: (String?) -> Unit) {
        if (imageUri != null) {
            var fileName = "${System.currentTimeMillis()}.jpg"

            var uploadTask = storageRef.child("product").child(fileName).putFile(imageUri!!)

            uploadTask.continueWithTask {
                if (!it.isSuccessful) {
                    it.exception?.let {
                        //throw it
                        onComplete(null)
                    }
                }
                storageRef.child("product").child(fileName).downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val downloadUrl = it.result.toString()
                    onComplete(downloadUrl)
                } else {
                    onComplete(null)
                }
            }
        } else {
            onComplete(existingUrl)

        }
    }


}
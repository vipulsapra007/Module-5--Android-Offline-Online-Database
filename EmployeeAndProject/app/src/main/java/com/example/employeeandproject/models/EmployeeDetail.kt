package com.example.employeeandproject.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeDetail(
    var id:String?="",
    var fname:String="",
    var lname:String="",
    var email:String="",
    var contact:String="",
    var password:String="",
    var profileUrl: String="",
    var createdAt:Long=System.currentTimeMillis()


):Parcelable

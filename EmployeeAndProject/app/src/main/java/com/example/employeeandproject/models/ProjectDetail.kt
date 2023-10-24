package com.example.employeeandproject.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectDetail(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var createdAt:Long=System.currentTimeMillis()
) : Parcelable
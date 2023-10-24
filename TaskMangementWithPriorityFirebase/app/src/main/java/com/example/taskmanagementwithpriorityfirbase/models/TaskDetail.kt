package com.example.taskmanagementwithpriorityfirbase.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskDetail (
    var id:String="",
    var title:String="",
    var desc:String="",
    var priority:Int=0,
    var status:Int=0,
    var timeStamp:Long=System.currentTimeMillis()
    ):Parcelable
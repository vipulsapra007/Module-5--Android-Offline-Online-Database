package com.example.taskmanagementwithsqllite.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task-table")
data class TaskDetail(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var title:String,
    var desc:String,
    var status:Int=0,
    var createdAt:Long=System.currentTimeMillis()
):Parcelable

package com.example.stickynotes.Database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sticky-notes")
data class StickyNotes(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var description: String,
    var createdAt:Long=System.currentTimeMillis()
) : Parcelable

package com.example.taskmanagementwithsqllite.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskmanagementwithsqllite.model.TaskDetail

@Database(entities = [TaskDetail::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun taskDao():TaskDAO


}
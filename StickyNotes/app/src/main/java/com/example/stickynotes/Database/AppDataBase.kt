package com.example.stickynotes.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stickynotes.Database.DAO.StickyNotesDAO
import com.example.stickynotes.Database.entity.StickyNotes

@Database(entities = [StickyNotes::class], version = 1)
abstract class AppDataBase:RoomDatabase() {

    abstract fun stickyNotesDao():StickyNotesDAO



}
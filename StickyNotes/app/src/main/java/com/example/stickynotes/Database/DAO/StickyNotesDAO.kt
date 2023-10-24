package com.example.stickynotes.Database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stickynotes.Database.entity.StickyNotes

@Dao
interface StickyNotesDAO {

    @Insert
    fun insertStickyNote(stickyNote: StickyNotes)

    @Query("Select *from `sticky-notes`")
    fun getNotesList(): List<StickyNotes>

    @Query("delete from `sticky-notes` where id=:uid")
    fun deleteNotesList(uid: Int)

   @Update
   fun updateNotesList(stickyNote: StickyNotes)
}
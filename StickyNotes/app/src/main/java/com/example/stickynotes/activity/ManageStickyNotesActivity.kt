package com.example.stickynotes.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.example.stickynotes.Database.AppDataBase
import com.example.stickynotes.Database.entity.StickyNotes
import com.example.stickynotes.databinding.ActivityManageStickyNotesBinding
import java.lang.Exception

class ManageStickyNotesActivity : AppCompatActivity() {
    lateinit var db: AppDataBase

    lateinit var binding: ActivityManageStickyNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageStickyNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "sticky.db")
            .allowMainThreadQueries().build()

        var notes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("NOTES", StickyNotes::class.java)
        } else {
            intent.getParcelableExtra("NOTES")
        }

        notes?.let {

            binding.btnAdd.text = "Update Category"
            binding.etTitle.setText(notes.title)
            binding.etDescription.setText(notes.description)
        }




        binding.btnAdd.setOnClickListener {

            var title = binding.etTitle.text.toString().trim()
            var description = binding.etDescription.text.toString().trim()

            if (notes != null) {

                notes.title = title
                notes.description = description
                updateNotes(notes)
            }else {

                addStickyNote(title, description)
            }

        }


    }

    private fun updateNotes(notes: StickyNotes) {

        try {
            db.stickyNotesDao().updateNotesList(notes)
            Toast.makeText(applicationContext, "Notes Updated", Toast.LENGTH_SHORT).show()
            onBackPressed()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun addStickyNote(title: String, description: String) {

        var note = StickyNotes(title = title, description = description)
        try {
            db.stickyNotesDao().insertStickyNote(note)
            Toast.makeText(applicationContext, "Sticky Note Added", Toast.LENGTH_SHORT).show()
            onBackPressed()

        } catch (e: Exception) {

            e.printStackTrace()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
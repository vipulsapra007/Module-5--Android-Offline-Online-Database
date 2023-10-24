package com.example.stickynotes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.stickynotes.Adapter.StickyNoteAdapter
import com.example.stickynotes.Database.AppDataBase
import com.example.stickynotes.Database.entity.StickyNotes
import com.example.stickynotes.databinding.ActivityStickyNotesListBinding
import java.lang.Exception

class StickyNotesListActivity : AppCompatActivity() {
    var notesList = mutableListOf<StickyNotes>()
    lateinit var db: AppDataBase
    lateinit var stickyNotesAdapter: StickyNoteAdapter
    lateinit var binding: ActivityStickyNotesListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStickyNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "sticky.db")
            .allowMainThreadQueries().build()


        stickyNotesAdapter = StickyNoteAdapter(this, notesList)
        binding.recylerView.layoutManager = LinearLayoutManager(this)
        binding.recylerView.adapter = stickyNotesAdapter





        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, ManageStickyNotesActivity::class.java))
        }

        stickyNotesAdapter.setOnItemClickListener(object : StickyNoteAdapter.OnItemClickListener {
            override fun onItemClicked(notes: StickyNotes) {
                var intent = Intent(applicationContext, ManageStickyNotesActivity::class.java)
                intent.putExtra("NOTES", notes)
                startActivity(intent)


            }

            override fun onItemDeleted(notes: StickyNotes) {
                try {
                    db.stickyNotesDao().deleteNotesList(notes.id)
                    getList()
                    Toast.makeText(applicationContext, "Sticky Note Deleted", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        })

    }

    override fun onResume() {
        super.onResume()
        if (db != null)
            getList()

    }

    private fun getList() {
        notesList = db.stickyNotesDao().getNotesList() as MutableList<StickyNotes>
        if (notesList.size > 0) {
            stickyNotesAdapter.setItems(notesList)
        }
    }
}
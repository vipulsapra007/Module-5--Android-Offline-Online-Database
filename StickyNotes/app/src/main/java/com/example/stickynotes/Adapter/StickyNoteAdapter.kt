package com.example.stickynotes.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.stickynotes.Database.entity.StickyNotes
import com.example.stickynotes.R
import com.example.stickynotes.databinding.ActivityStickyNotesListBinding
import com.example.stickynotes.databinding.StickyNotesListBinding
import java.text.SimpleDateFormat
import java.util.Date

class StickyNoteAdapter(var context: Context, var stickyNotesList: MutableList<StickyNotes>) :
    Adapter<StickyNoteAdapter.MyViewHolder>() {
    lateinit var binding: StickyNotesListBinding


    class MyViewHolder(var bind: StickyNotesListBinding) : ViewHolder(bind.root)
lateinit var listener:OnItemClickListener
interface OnItemClickListener{

    fun onItemClicked(notes:StickyNotes)
    fun onItemDeleted(notes: StickyNotes)
}
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.listener=onItemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding =
            StickyNotesListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stickyNotesList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var notes = stickyNotesList[position]
        holder.bind.tvTitle.text = "${notes.title}"
        holder.bind.tvDesc.text = "${notes.description}"
        holder.bind.tvCreatedAt.text = "CreatedAt: ${TimeFormat(notes.createdAt)}"
        holder.bind.cardView.setBackgroundResource(RandomColor())

        holder.bind.ivDelete.setOnClickListener {
            listener.onItemDeleted(notes)
        }

        holder.bind.cardView.setOnClickListener {
            listener.onItemClicked(notes)
        }

    }

    private fun RandomColor(): Int {
        var red = R.color.red
        var yellow = R.color.yellow
        var blue = R.color.blue
        var brown = R.color.brown
        var colorList = mutableListOf(red, yellow, blue, brown)

        return colorList.shuffled().last()


    }

    fun setItems(notesList: MutableList<StickyNotes>) {
        this.stickyNotesList = notesList
        notifyDataSetChanged()

    }

}

private fun TimeFormat(createdAt: Long): CharSequence? {
    var timeStamp = createdAt
    val date = Date(timeStamp)
    val dateFormat = SimpleDateFormat("HH:mm(dd-MM-yyyy)")
    return dateFormat.format(date)
}





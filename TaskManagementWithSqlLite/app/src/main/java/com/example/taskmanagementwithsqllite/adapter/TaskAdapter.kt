package com.example.taskmanagementwithsqllite.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskmanagementwithsqllite.R
import com.example.taskmanagementwithsqllite.databinding.TasklistLayoutBinding
import com.example.taskmanagementwithsqllite.model.TaskDetail
import java.sql.Date
import java.text.SimpleDateFormat

class TaskAdapter(var context: Context,var taskList: MutableList<TaskDetail>):Adapter<TaskAdapter.MyViewHolder>() {
    lateinit var binding: TasklistLayoutBinding
    lateinit var listener: OnItemClickListener


    interface OnItemClickListener {
        fun onItemDeleted(task: TaskDetail)
        fun onItemEdit(task: TaskDetail)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.listener = onItemClickListener

    }

    class MyViewHolder(var bind: TasklistLayoutBinding) : ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = TasklistLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var task = taskList[position]
        holder.bind.tvTitle.text = "${task.title}"
        holder.bind.tvDesc.text = "${task.desc}"

        var taskStatus = if (task.status == 1) {

            holder.bind.tvTstatus.setTextColor(Color.parseColor("#3F51B5"))
            "Task Status:COMPLETED"
        } else {
            holder.bind.tvTstatus.setTextColor(Color.parseColor("#F44336"))
            "Task Status:UPCOMING"
        }


        holder.bind.tvCreatedAt.text=convertLongToTime(task.createdAt)

        holder.bind.tvTstatus.text = taskStatus


        holder.bind.ivDlt.setOnClickListener {

            listener.onItemDeleted(task)


        }
        holder.bind.ivEdit.setOnClickListener {
            listener.onItemEdit(task)

        }
    }

    fun setItems(taskList: MutableList<TaskDetail>) {
        this.taskList = taskList
        notifyDataSetChanged()

    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm [dd.MM.yyyy] ")
        return format.format(date)

    }
}
package com.example.taskmanagementwithpriorityfirbase.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskmanagementwithpriorityfirbase.R
import com.example.taskmanagementwithpriorityfirbase.databinding.TaskListLayoutBinding
import com.example.taskmanagementwithpriorityfirbase.models.TaskDetail
import java.sql.Date
import java.text.SimpleDateFormat

class TaskListAdapter(var context: Context, var taskDetail: MutableList<TaskDetail>) :
    Adapter<TaskListAdapter.MyViewHolder>() {
    lateinit var binding: TaskListLayoutBinding

    class MyViewHolder(var bind: TaskListLayoutBinding) : ViewHolder(bind.root)

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onEditClickListener(taskDetail: TaskDetail)
        fun onDeleteClickListener(taskDetail: TaskDetail)
        fun onStatusUpdateListener(taskDetail: TaskDetail)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {

        this.listener = onItemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = TaskListLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskDetail.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var task = taskDetail[position]
        holder.bind.tvTitle.text = task.title
        holder.bind.tvDesc.text = task.desc

        holder.bind.tvCreatedAt.text = convertLongToTime(task.timeStamp)

        holder.bind.ivEdit.setOnClickListener {
            listener.onEditClickListener(task)
        }
        holder.bind.ivDlt.setOnClickListener {
            listener.onDeleteClickListener(task)
        }
        holder.bind.btnTstatus.setOnClickListener {

        listener.onStatusUpdateListener(task)

        }

        if (task.status!=0){
            holder.bind.btnTstatus.text="Task Completed"
            holder.bind.cardView.setCardBackgroundColor(Color.WHITE)

            if (task.priority==1){
                holder.bind.tvPriority.text="Priority:HIGH "
            }else if (task.priority==2){
                holder.bind.tvPriority.text="Priority:AVERAGE "
            }else if (task.priority==3){
                holder.bind.tvPriority.text="Priority:LOW "
            }
        }else if (task.priority==1 ){
            holder.bind.cardView.setCardBackgroundColor(Color.RED)
           holder.bind.tvPriority.text="Priority:HIGH "
        }else if (task.priority==2){
            holder.bind.cardView.setCardBackgroundColor(Color.BLUE)
            holder.bind.tvPriority.text="Priority:AVERAGE "
        }else if (task.priority==3){
            holder.bind.cardView.setCardBackgroundColor(Color.GREEN)
            holder.bind.tvPriority.text="Priority:LOW "
        }


    }


    fun setItems(taskList: MutableList<TaskDetail>) {
        this.taskDetail = taskList
        this.taskDetail.sortByDescending { taskDetail ->taskDetail.timeStamp  }
        notifyDataSetChanged()
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm [dd.MM.yyyy] ")
        return format.format(date)

    }


}
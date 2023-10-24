package com.example.employeeandproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.employeeandproject.databinding.ProjectListLayoutBinding
import com.example.employeeandproject.models.ProjectDetail
import java.sql.Date
import java.text.SimpleDateFormat

class ProjectDetailAdapter(var context: Context,var projectList: MutableList<ProjectDetail>):Adapter<ProjectDetailAdapter.MyViewHolder>() {
lateinit var binding: ProjectListLayoutBinding

    class MyViewHolder(var bind:ProjectListLayoutBinding):ViewHolder(bind.root)
    lateinit var listener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClicked(product: ProjectDetail)
        fun onItemDeleted(product:ProjectDetail)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.listener=onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding= ProjectListLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var project=projectList[position]
        holder.bind.tvTitle.text=project.title
        holder.bind.tvDesc.text=project.description

        holder.bind.ivDlt.setOnClickListener {

            listener.onItemDeleted(project)
        }

        holder.bind.ivEdit.setOnClickListener {
            listener.onItemClicked(project)
        }
        holder.bind.tvCreatedAt.text=convertLongToTime(project.createdAt)

    }
    fun setItems(projectList: MutableList<ProjectDetail>){
        this.projectList=projectList
        notifyDataSetChanged()
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm [dd.MM.yyyy] ")
        return format.format(date)

    }


}
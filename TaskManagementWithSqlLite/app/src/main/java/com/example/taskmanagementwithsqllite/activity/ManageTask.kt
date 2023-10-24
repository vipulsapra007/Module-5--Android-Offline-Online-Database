package com.example.taskmanagementwithsqllite.activity

import android.R
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.room.Room
import com.example.taskmanagementwithsqllite.database.AppDatabase
import com.example.taskmanagementwithsqllite.databinding.ActivityManageTaskBinding
import com.example.taskmanagementwithsqllite.model.TaskDetail
import java.lang.Exception

class ManageTask : AppCompatActivity() {
    lateinit var db: AppDatabase
    private var taskStatusList = listOf("Select Status", "Completed", "Upcoming")
    private var taskStatusId = 0
    private lateinit var taskstatusAdapter: ArrayAdapter<String>

    lateinit var binding: ActivityManageTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task.db")
            .allowMainThreadQueries().build()

        taskstatusAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, taskStatusList)
        binding.spinner.adapter = taskstatusAdapter

        var taskDetail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TASK DETAIL", TaskDetail::class.java)
        } else {
            intent.getParcelableExtra("TASK DETAIL")
        }

        taskDetail?.let {

            binding.btnAdd.text = "UPDATE DETAIL"
            binding.etTitle.setText(it.title)
            binding.etDesc.setText(it.desc)
            taskStatusId = it.status
            binding.spinner.setSelection(taskStatusId)

        }

        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskStatusId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }





        binding.btnAdd.setOnClickListener {

            var title = binding.etTitle.text.toString().trim()
            var desc = binding.etDesc.text.toString().trim()

            if (taskDetail != null) {

                taskDetail.title=title
                taskDetail.desc=desc
                taskDetail.status=taskStatusId

                updateTask(taskDetail)

            } else {
                if (taskStatusId == 0) {

                    Toast.makeText(this, "Please Select Task Status ", Toast.LENGTH_SHORT).show()
                } else {
                    addTask(title, desc, taskStatusId)
                }
            }

        }


    }

    private fun updateTask(taskDetail: TaskDetail) {
        try {
            db.taskDao().updateTask(taskDetail)
            Toast.makeText(applicationContext, "Detail Updated", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }catch (e:Exception){

            e.printStackTrace()
        }

    }


    @SuppressLint("SuspiciousIndentation")
    private fun addTask(title: String, desc: String, taskStatus: Int) {

        var taskDetail = TaskDetail(title = title, desc = desc, status = taskStatus)
        try {
            db.taskDao().insertTask(taskDetail)
            Toast.makeText(applicationContext, "Task Added", Toast.LENGTH_SHORT).show()
            onBackPressed()
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

}
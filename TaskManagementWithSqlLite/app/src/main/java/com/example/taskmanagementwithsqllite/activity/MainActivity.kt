package com.example.taskmanagementwithsqllite.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.taskmanagementwithsqllite.adapter.TaskAdapter
import com.example.taskmanagementwithsqllite.database.AppDatabase
import com.example.taskmanagementwithsqllite.databinding.ActivityMainBinding
import com.example.taskmanagementwithsqllite.model.TaskDetail
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var db:AppDatabase
    lateinit var taskAdapter: TaskAdapter
    private var taskList= mutableListOf<TaskDetail>()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "task.db")
            .allowMainThreadQueries().build()

        taskAdapter= TaskAdapter(this,taskList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=taskAdapter

        binding.btnAdd.setOnClickListener {

            startActivity(Intent(this, ManageTask::class.java))

        }

        taskAdapter.setOnItemClickListener(object : TaskAdapter.OnItemClickListener {
            override fun onItemDeleted(task: TaskDetail) {
                try {
                    db.taskDao().deleteTask(task.id)
                    getList()
                    Toast.makeText(applicationContext, "Task Deleted", Toast.LENGTH_SHORT).show()

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onItemEdit(task: TaskDetail) {
                var intent=Intent(applicationContext,ManageTask::class.java)
                intent.putExtra("TASK DETAIL",task)
                startActivity(intent)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        if(db!=null){
            getList()
        }
    }

    private fun getList() {
        taskList=db.taskDao().getTaskList() as MutableList<TaskDetail>
        if (taskList.size>0){
            taskAdapter.setItems(taskList)
        }
    }
}
package com.example.taskmanagementwithpriorityfirbase.activites

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.PopupMenu.OnMenuItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanagementwithpriorityfirbase.R
import com.example.taskmanagementwithpriorityfirbase.adapter.TaskListAdapter
import com.example.taskmanagementwithpriorityfirbase.databinding.ActivityTaskListBinding
import com.example.taskmanagementwithpriorityfirbase.models.TaskDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TaskListActivity : AppCompatActivity() {
    private var Reference = Firebase.database.reference
    lateinit var binding: ActivityTaskListBinding
    private var taskList = mutableListOf<TaskDetail>()

    lateinit var taskAdapter: TaskListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskAdapter = TaskListAdapter(this, taskList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = taskAdapter

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, ManageTaskActivity::class.java))
        }

        Reference.child("TASK DETAIL NODE").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                for (snap in snapshot.children) {
                    var task = snap.getValue(TaskDetail::class.java)
                    if (task != null) {
                        taskList.add(task)
                    }
                }
                taskAdapter.setItems(taskList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        taskAdapter.setOnItemClickListener(object : TaskListAdapter.OnItemClickListener {
            override fun onEditClickListener(taskDetail: TaskDetail) {
                var intent = Intent(applicationContext, ManageTaskActivity::class.java)
                intent.putExtra("TASK DETAIL", taskDetail)
                startActivity(intent)
            }

            override fun onDeleteClickListener(taskDetail: TaskDetail) {
                Reference.child("TASK DETAIL NODE").child(taskDetail.id).removeValue()
                Toast.makeText(applicationContext, "Task Successfully Removed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onStatusUpdateListener(taskDetail: TaskDetail) {

                var popupMenu = PopupMenu(applicationContext, taskAdapter.binding.btnTstatus)
                menuInflater.inflate(R.menu.task_status_menu, popupMenu.menu)
                popupMenu.show()

                popupMenu.setOnMenuItemClickListener(object : OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        return when (item!!.itemId) {
                            R.id.t_pending -> {
                                taskDetail.id?.let {

                                    var taskStatus=TaskDetail(title = taskDetail.title, desc = taskDetail.desc, priority = taskDetail.priority, status = 0, timeStamp = taskDetail.timeStamp, id = it)
                                    Reference.child("TASK DETAIL NODE").child(it).setValue(taskStatus).addOnSuccessListener {
                                        Toast.makeText(applicationContext, "Task Pending", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                true
                            }

                            R.id.t_complete -> {
                              taskDetail.id?.let {

                                  var taskStatus=TaskDetail(title = taskDetail.title, desc = taskDetail.desc, priority = taskDetail.priority, status = 1, timeStamp = taskDetail.timeStamp, id = it)
                                  Reference.child("TASK DETAIL NODE").child(it).setValue(taskStatus).addOnSuccessListener {
                                      Toast.makeText(applicationContext, "Task Completed", Toast.LENGTH_SHORT).show()
                                  }
                              }
                                true
                            }

                            else -> false
                        }
                    }
                })

            }

        })


    }


}







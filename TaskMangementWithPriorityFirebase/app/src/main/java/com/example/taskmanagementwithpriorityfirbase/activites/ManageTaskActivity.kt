package com.example.taskmanagementwithpriorityfirbase.activites

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.taskmanagementwithpriorityfirbase.R
import com.example.taskmanagementwithpriorityfirbase.databinding.ActivityManageTaskBinding
import com.example.taskmanagementwithpriorityfirbase.models.TaskDetail
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ManageTaskActivity : AppCompatActivity() {

    private var taskDetail: TaskDetail? = null
    private var priority =
        listOf("Select Piority", "High Priority", "Average Priority", "Low Priority")
    private var priortyStatus: Int = 0
    private var Refrence = Firebase.database.reference
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var binding: ActivityManageTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priority)
        binding.spinner.adapter = spinnerAdapter

        taskDetail=  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TASK DETAIL",TaskDetail::class.java)
        }else{
            intent.getParcelableExtra("TASK DETAIL")
        }



        taskDetail?.let {

            binding.etTitle.setText(taskDetail!!.title)
            binding.etDesc.setText(taskDetail!!.desc)
            priortyStatus=taskDetail!!.priority


            var index=taskDetail!!.priority

            binding.spinner.setSelection(index)

        }


        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                 position: Int,
                id: Long
            ) {
                priortyStatus=position

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {



            }
        }







        binding.btnAddtask.setOnClickListener {
            var title = binding.etTitle.text.toString().trim()
            var desc = binding.etDesc.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "Please Provide Title", Toast.LENGTH_SHORT).show()
            } else if (priortyStatus == 0) {
                Toast.makeText(this, "Please Select Priority", Toast.LENGTH_SHORT).show()
            } else if (desc.isEmpty()) {
                Toast.makeText(this, "Please Provide Description", Toast.LENGTH_SHORT).show()
            } else {

                addContentView(title, desc, priortyStatus)
            }
        }


    }

    private fun addContentView(title: String, desc: String, priortyStatus: Int) {
        var key = if (taskDetail != null) {
            taskDetail!!.id
        } else {
            Refrence.push().key
        }

        key?.let {

            var taskDetail =
                TaskDetail(title = title, desc = desc, id = it, priority = priortyStatus)
            Refrence.child("TASK DETAIL NODE").child(taskDetail.id).setValue(taskDetail)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Task Added", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }.addOnFailureListener {
                Toast.makeText(applicationContext, "Try Again Later", Toast.LENGTH_SHORT).show()
            }

        }


    }

}
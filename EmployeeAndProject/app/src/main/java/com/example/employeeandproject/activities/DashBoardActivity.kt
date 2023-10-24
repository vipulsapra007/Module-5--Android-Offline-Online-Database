package com.example.employeeandproject.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeeandproject.R
import com.example.employeeandproject.adapter.ProjectDetailAdapter
import com.example.employeeandproject.databinding.ActivityDashBoardBinding
import com.example.employeeandproject.fragment.BottomSheetFragment
import com.example.employeeandproject.models.EmployeeDetail
import com.example.employeeandproject.models.ProjectDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashBoardActivity : AppCompatActivity() {
    private lateinit var mRef: DatabaseReference
    private lateinit var db: FirebaseDatabase
    lateinit var binding: ActivityDashBoardBinding
    private lateinit var projectAdapter: ProjectDetailAdapter
    private var projectList = mutableListOf<ProjectDetail>()
    lateinit var auth: FirebaseAuth
    private var employeeDetail: EmployeeDetail? = null
    private var project: ProjectDetail?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        auth = Firebase.auth
        db = Firebase.database
        mRef = db.reference


        projectAdapter = ProjectDetailAdapter(this, projectList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = projectAdapter


        projectAdapter.setOnItemClickListener(object : ProjectDetailAdapter.OnItemClickListener {
            override fun onItemClicked(project: ProjectDetail) {
                BottomSheetFragment(project).show(supportFragmentManager, "NEW PROJECT TAG")


            }

            override fun onItemDeleted(product: ProjectDetail) {
                mRef.child("PROJECT DATA NODE").child(auth.currentUser!!.uid).child(product.id).removeValue()
                Toast.makeText(applicationContext, "Item Deleted", Toast.LENGTH_SHORT).show()
            }

        })


        mRef.child("PROJECT NODE").child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                projectList.clear()
                for (snap in snapshot.children){
                    var project=snap.getValue(ProjectDetail::class.java)
                    if (project != null) {
                        projectList.add(project)
                    }
                }
                projectAdapter.setItems(projectList)


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Techincal Error", Toast.LENGTH_SHORT).show()
            }
        })


        binding.btnAdd.setOnClickListener {

            BottomSheetFragment(project).show(supportFragmentManager, "NEW PROJECT TAG")


        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.menu_profile -> {
                var intent=Intent(this, ManageProfileActivity::class.java)
                intent.putExtra("EMPLOYEE-DETAIL",employeeDetail)
                startActivity(intent)
                true
            }

            R.id.menu_contact -> {
                Toast.makeText(this, "You will get callback in 24 hours", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_logout -> {

                Firebase.auth.signOut()
                Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }

}
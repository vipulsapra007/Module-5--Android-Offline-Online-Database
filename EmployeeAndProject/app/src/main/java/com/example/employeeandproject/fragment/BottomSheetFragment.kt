package com.example.employeeandproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.employeeandproject.R
import com.example.employeeandproject.activities.DashBoardActivity
import com.example.employeeandproject.databinding.FragmentBottomSheetBinding
import com.example.employeeandproject.models.ProjectDetail
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class BottomSheetFragment(var project:ProjectDetail?) : BottomSheetDialogFragment() {
    lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentBottomSheetBinding

    private lateinit var mRef: DatabaseReference
    private lateinit var pRef: DatabaseReference
    private lateinit var db: FirebaseDatabase



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        db = Firebase.database
        mRef = db.reference


        project?.let {

            binding.etTitle.setText(project!!.title)
            binding.etDesc.setText(project!!.description)
        }


        binding.btnSave.setOnClickListener {

            var title = binding.etTitle.text.toString().trim()
            var desc = binding.etDesc.text.toString().trim()

            addUpdate(title, desc)
        }
    }

    private fun addUpdate(title: String, desc: String) {

        var key = if (project != null) {
            project!!.id

        } else {
            mRef.push().key
        }

        key?.let {

            var project = ProjectDetail(id = it, title = title, description = desc)
            mRef.child("PROJECT NODE").child(auth.currentUser!!.uid).child(project.id).setValue(project).addOnSuccessListener {
                Toast.makeText(requireActivity(), "Project Added", Toast.LENGTH_SHORT).show()
                var intent = Intent(requireActivity(), DashBoardActivity::class.java)
                intent.putExtra("PROJECT", project)
                startActivity(intent)
                dismiss()
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "Try Again Later", Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }


}
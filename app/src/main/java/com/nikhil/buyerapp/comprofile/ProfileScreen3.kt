package com.nikhil.buyerapp.comprofile

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nikhil.buyerapp.R
import com.nikhil.buyerapp.databinding.ActivityProfileScreen3Binding

class ProfileScreen3 : AppCompatActivity() {
    lateinit var binding: ActivityProfileScreen3Binding
    val db=Firebase.firestore
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val interests = listOf(
        "Graphic Design", "Web Development", "Android Development",
        "Content Writing", "Video Editing", "Data Analysis"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileScreen3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_interest, interests)
        binding.actState.setAdapter(adapter)
        binding.actState1.setAdapter(adapter)
        binding.actState2.setAdapter(adapter)
        binding.actState.setOnClickListener {
            binding.actState.showDropDown()
        }
        binding.actState1.setOnClickListener {
            binding.actState1.showDropDown()
        }
        binding.actState2.setOnClickListener {
            binding.actState2.showDropDown()
        }
//        binding.btnFinish.setOnClickListener {
//            val uid=auth.currentUser?.uid
//            val int1 = binding.actState.text.toString()
//            val int2 = binding.actState.text.toString()
//            val int3 = binding.actState.text.toString()
//            if(int1.isNotBlank() && int2.isNotBlank() && int3.isNotBlank() )
//            {
//                if (uid != null) {
//                    db.collection("Users").document(uid).update()
//                }
//            }
//        }
    }
}
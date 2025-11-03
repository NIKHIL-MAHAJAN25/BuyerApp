package com.nikhil.buyerapp.comprofile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nikhil.buyerapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

import com.nikhil.buyerapp.databinding.ActivityEnterCodeBinding

class EnterCode : AppCompatActivity() {
    lateinit var binding: ActivityEnterCodeBinding
    private var auth: FirebaseAuth =FirebaseAuth.getInstance()
    val auid=auth.currentUser?.uid
    val db=Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEnterCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnVerifyOtp.setOnClickListener {
            if (auid != null) {
                fetchcode(auid)
            }
        }
    }
    fun fetchcode(auid:String){
            db.collection("Users").document(auid).get().addOnSuccessListener {document->
                if(document!=null && document.exists()){
                    val securecode=document.getString("approvalCode")
                    Log.d("code","Code:$securecode")
                }
            }
    }
}
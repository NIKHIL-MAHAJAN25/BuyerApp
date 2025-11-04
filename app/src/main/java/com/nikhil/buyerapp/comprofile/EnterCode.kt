package com.nikhil.buyerapp.comprofile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nikhil.buyerapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
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
        val boxes = listOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )
        for(i in boxes.indices)
        {
            boxes[i].addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
                {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(s?.length==1 && i<boxes.size-1)
                    {
                        boxes[i+1].requestFocus()
                    }
                    else if(s.isNullOrEmpty()&& i>0)
                    {
                        boxes[i-1].requestFocus()
                    }

                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
        binding.btnVerifyOtp.setOnClickListener {
            val code = boxes.joinToString("") { it.text.toString() }

            if (auid != null) {
                fetchcode(auid){securecode->
                    if(securecode!=null && securecode==code)
                    {
                        db.collection("Users").document(auid).update("approved",true)
                        fetchname(auid){name,location->
                            val lancer= mapOf(
                                "name" to name,
                                "uid" to auid,
                                "state" to location
                            )
                            db.collection("Client").document(auid).set(lancer, SetOptions.merge())

                        }
                        Log.d("otp","otp verified")
//                        startActivity(Intent(this,HomeActivity::class.java))
                    }
                    else{
                        Log.d("otp", "Invalid OTP ❌")

                    }
                }
            }
        }
    }
    fun fetchname(auid: String,onResult: (String?,String?) -> Unit)
    {
        if(auid!=null)
        {
            db.collection("Users").document(auid).get().addOnSuccessListener { document->
                if(document!=null && document.exists())
                {
                    val name = document.getString("fullName")
                    val location=document.getString("state")
                    onResult(name,location)
                }else{
                    onResult(null,null)
                }
            }
        }
    }
    fun fetchcode(auid:String,onResult: (String?)->Unit){


            db.collection("Users").document(auid).get().addOnSuccessListener {document->
                if(document!=null && document.exists()){
                    val securecode=document.getString("approvalCode")
                    Log.d("code","Code:$securecode")
                    onResult(securecode)
                }
                else{
                    onResult(null)
                }
            }
    }
}
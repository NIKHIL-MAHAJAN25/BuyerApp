package com.nikhil.buyerapp.freelanceprofileview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.nikhil.buyerapp.R
import com.nikhil.buyerapp.databinding.FragmentExperienceBinding
import com.nikhil.buyerapp.dataclasses.Experience
import com.nikhil.buyerapp.dataclasses.Freelancer


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExperienceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExperienceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentExperienceBinding?=null
    var userlist= arrayListOf<Experience>()
    private val binding get()=_binding!!
    val db= Firebase.firestore
    lateinit var uid:String
    lateinit var adapterr:ExpAdapter

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid=arguments?.getString("uid")!!
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentExperienceBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterr = ExpAdapter(userlist)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapterr
        userlist.clear()
        startlisten()
    }
    private fun startlisten() {
        if (uid != null) {
            db.collection("Freelancers").document(uid).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore Error", "Listen failed.", error)
                    return@addSnapshotListener
                }
                if(snapshot!=null && snapshot.exists()){
                    val freelancer=snapshot.toObject(Freelancer::class.java)
                    val newexp=freelancer?.experience?: emptyList()
                    adapterr.updateData(newexp)
                }else{
                    Log.d("Firestore Info", "Current data: null")

                }
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExperienceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExperienceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
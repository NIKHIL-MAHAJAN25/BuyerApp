package com.nikhil.buyerapp.freelanceprofileview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.nikhil.buyerapp.R
import com.nikhil.buyerapp.databinding.FragmentSkillsBinding
import com.nikhil.buyerapp.dataclasses.Freelancer
import com.nikhil.buyerapp.skills.SkillsCat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SkillsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SkillsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSkillsBinding?=null
    private val binding get()=_binding!!
    val db= Firebase.firestore
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var userlist= arrayListOf<SkillsCat>()
    lateinit var uid: String
    private var sclist= mutableListOf<SkillsCat>()
    lateinit var skadapter:skillsadapter



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
        _binding=FragmentSkillsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setuprecycler()
        loadskills()
    }
    private fun setuprecycler(){
        skadapter= skillsadapter(sclist)
        binding.recyclerskills.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=skadapter
        }
    }
    // in function we convert freelancer to object to fetch info
    private fun loadskills(){
        if(uid==null){
            return
        }
        db.collection("Freelancers").document(uid).get()
            .addOnSuccessListener { document->
                if(document.exists()){
                    val free=document.toObject<Freelancer>()
                    val flatskilllist=free?.skills?: emptyList()
                    val groupedmap=flatskilllist.groupBy { it.category }
                    val catlist=groupedmap.map { (categoryName,skillsInCat)->
                        SkillsCat(
                            categoryName=categoryName,
                            skills = skillsInCat.map{it.name}
                        )
                    }
                    skadapter.updatedata(catlist)

                }
            }
            .addOnFailureListener {
                showSnackbar("Error fetching skills:")
            }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SkillsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SkillsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun showSnackbar(message:String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
package com.nikhil.buyerapp.basichome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import retrofit2.Callback

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.nikhil.buyerapp.BuildConfig
import com.nikhil.buyerapp.R
import com.nikhil.buyerapp.databinding.FragmentHome2Binding
import com.nikhil.buyerapp.databinding.FragmentHomeBinding
import com.nikhil.buyerapp.news.NewsAdapter
import com.nikhil.buyerapp.news.NewsResponse
import com.nikhil.buyerapp.news.Result
import com.nikhil.buyerapp.news.RetroNews
import com.nikhil.buyerapp.utils.logd
import com.nikhil.buyerapp.utils.loge
import com.nikhil.buyerapp.utils.snack
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding:FragmentHome2Binding?=null
    private val binding get() =_binding!!
    lateinit var serviceAdapter: ServiceAdapter
    lateinit var newsAdapter: NewsAdapter
    private var firestoreListener:ListenerRegistration?=null
    private val auth:FirebaseAuth=FirebaseAuth.getInstance()
    private val db=Firebase.firestore
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentHome2Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        setupnews()
        loadinfo()
        fetchnews()
        binding.seealljobs.setOnClickListener {
            fetchnews()
        }
    }
    private fun fetchnews()
    {
        val apikey="${BuildConfig.NEWS_KEY}"
        val query="Artificial Intelligence, Machine learning"

        RetroNews.instance.getnews(apikey,query,size=8).enqueue(object : Callback<NewsResponse>{
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
               if(_binding==null) return
                if(response.isSuccessful)
                {
                    val rawList = response.body()?.results

                    // --- THE BOUNCER (Filtering Logic) ---
                    val cleanList = rawList?.filter { article ->
                        // Rule: "You can only enter if you have a non-empty Image URL"
                        !article.image_url.isNullOrBlank()
                    }

                    if(!cleanList.isNullOrEmpty())
                    {
                        newsAdapter.submitList(cleanList)
                    }else{
                        loge("News error:${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                if (_binding == null) return
                loge("News API Failed", t)
                snack("Failed to load news")
            }

        })
    }
    private fun setupnews()
    {
        newsAdapter=NewsAdapter { link->
            try{
                val intent=Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }catch (e:Exception)
            {
                loge("could not open link",e)
            }
        }
        binding.recyclernews.apply{
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(requireContext())
            isNestedScrollingEnabled=false
        }
    }
    private fun setup(){
        serviceAdapter=ServiceAdapter()
        binding.recyclerservices.apply {
            adapter=serviceAdapter
        }
    }
    private fun loadinfo()
    {
        val uid=auth.currentUser?.uid
        firestoreListener?.remove()
        firestoreListener=db.collection("Skills").addSnapshotListener { snapshot, error ->
            if(error!=null)
            {
                loge("listen failed")
                return@addSnapshotListener
            }
//            lifecycle check
            if(_binding==null)
            {
                return@addSnapshotListener
            }
            if(snapshot!=null && !snapshot.isEmpty())
            {
                val skill=snapshot.toObjects(DataSkill::class.java)
                serviceAdapter.submitList(skill)
            }else{
                logd("null or empty")
                serviceAdapter.submitList(emptyList())

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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        firestoreListener?.remove()
        _binding=null
    }

}
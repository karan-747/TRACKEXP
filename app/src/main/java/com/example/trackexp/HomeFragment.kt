package com.example.trackexp

import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.trackexp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.util.Calendar

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var  calender:Calendar
    private lateinit var  adapter: HomeRVAdapter

    private val firebaseAuth =FirebaseAuth.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)



        homeFragmentViewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]

        binding.lifecycleOwner = this@HomeFragment
        binding.viewModel = homeFragmentViewModel


        calender =Calendar.getInstance()






       homeFragmentViewModel.checkIsRecordAvailable().observe(viewLifecycleOwner, Observer {
           if(it){
               binding.rvRecentTransaction.visibility = View.VISIBLE
               binding.tvNoRecentTransactions.visibility = View.GONE
           }else{
               binding.rvRecentTransaction.visibility = View.GONE
               binding.tvNoRecentTransactions.visibility = View.VISIBLE
           }
       })

        initRV()

        homeFragmentViewModel.getTheRecordsData().observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
        })

        binding.ivLogOut.setOnClickListener{


            val logOutDialog = AlertDialog.Builder(requireContext()).setCancelable(true)
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes",DialogInterface.OnClickListener(){
                    dialogInterface, i ->
                    homeFragmentViewModel.signOutUser()
                    it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                })
                .setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->

                }).create()

            logOutDialog.show()

        }


        binding.imageView14.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_analysisFragment)
        }
        binding.imageView8.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_analysisFragment)
        }

        binding.fabAddTransaction.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_transactionFragment)
        }
        binding.tvTransactionViewMore.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_transactionHistoryFragment)
        }
        binding.imageView9.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_transactionHistoryFragment)
        }



        binding.imageView6.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_editProfileFragment)
        }
        binding.textView23.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_editProfileFragment)
        }
        binding.tvUserName.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_editProfileFragment)
        }




        return binding.root
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.currentUser?.let { user->
            if(user.photoUrl != null){
                Log.d("FIREBASEUSERPHOTOURL" ,user.photoUrl.toString())
                Glide.with(this).load(user.photoUrl)
                    .error(R.drawable.img_user)
                    .into(binding.imageView6)

            }
        }
    }


    private fun initRV() {
        adapter = HomeRVAdapter(requireContext()){record: TransactionRecord ->
            navigateToEditFrag(record)
        }
        binding.rvRecentTransaction.adapter = adapter
        binding.rvRecentTransaction.layoutManager = LinearLayoutManager(context)



    }

    private fun navigateToEditFrag(record: TransactionRecord) {

        val finalBundle = Bundle()
        finalBundle.putStringArray("RECORD", arrayOf("yes",Gson().toJson(record)))

        binding.root.findNavController().navigate(R.id.action_homeFragment_to_editTransactionFragment,finalBundle)
    }

}
package com.example.trackexp

import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackexp.databinding.FragmentSignUpBinding
import com.example.trackexp.databinding.FragmentTransactionHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.util.Calendar


class TransactionHistoryFragment : Fragment(R.layout.fragment_transaction_history) {


    private lateinit var adapter: TransactionRecordRVAdapter
    private lateinit var binding: FragmentTransactionHistoryBinding
    private  lateinit var transactionHistoryVM: TransactionHistoryVM


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_transaction_history,container,false)
        transactionHistoryVM = ViewModelProvider(this)[TransactionHistoryVM::class.java]



        initRV()
        transactionHistoryVM.getUserAllRecords().observe(viewLifecycleOwner, Observer {


            if(it.size==0){
                binding.rvTransactions.visibility = View.GONE
                binding.tvNoTransactions.visibility = View.VISIBLE
            }else{
                binding.rvTransactions.visibility = View.VISIBLE
                binding.tvNoTransactions.visibility = View.GONE
            }

            adapter.updateList(it)
        })

        return binding.root
    }






    private fun openTransactionRecord(record:TransactionRecord){
        val bundle =  Bundle()
        bundle.putStringArray("RECORD" , arrayOf("no" ,Gson().toJson(record)))
        binding.root.findNavController().navigate(R.id.action_transactionHistoryFragment_to_editTransactionFragment, bundle)
    }
    private fun initRV() {
        adapter = TransactionRecordRVAdapter(requireContext()) { record: TransactionRecord ->
            openTransactionRecord(record)
        }
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter
    }
}
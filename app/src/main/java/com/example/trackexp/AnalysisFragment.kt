package com.example.trackexp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackexp.databinding.FragmentAnalysisBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class AnalysisFragment : Fragment(R.layout.fragment_analysis) {

    private lateinit var binding: FragmentAnalysisBinding
    private lateinit var analysisFragmentVM: AnalysisFragmentVM
    private  lateinit var  rvAdapter: AnalysisItemRVAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_analysis,container,false)
        analysisFragmentVM =ViewModelProvider(this)[AnalysisFragmentVM::class.java]
        binding.viewModel = analysisFragmentVM






        rvAdapter = AnalysisItemRVAdapter()

        //initTheDataDataSet()
        initRV()

        analysisFragmentVM.getTheDataListForAnalysis().observe(viewLifecycleOwner, Observer {
            rvAdapter.updateList(it)
        })




        return binding.root
    }

    private fun initRV() {

        binding.rvAnalysis.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }






}
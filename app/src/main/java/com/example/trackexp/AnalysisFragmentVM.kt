package com.example.trackexp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieEntry
import java.text.SimpleDateFormat
import java.util.Calendar

class AnalysisFragmentVM:ViewModel() {

    var repositoryRef :Repository? =null
    init {
        repositoryRef = Repository.getRepositoryInstance()
    }



    fun getTheMonthYear():String {

        return repositoryRef?.getMonthYear()!!
    }

    fun getTheDataListForAnalysis(): LiveData<ArrayList<ArrayList<PieEntry>>> {
        return repositoryRef?.getTheDataListForAnalysis()!!
    }
}
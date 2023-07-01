package com.example.trackexp

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.util.Calendar

class DateDialogClass(val myContext: Fragment): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val calender =Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val date = calender.get(Calendar.DATE)
        return DatePickerDialog(requireContext() , myContext as OnDateSetListener ,year,month,date)
    }
}
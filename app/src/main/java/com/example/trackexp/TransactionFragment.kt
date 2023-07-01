package com.example.trackexp

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.trackexp.databinding.FragmentTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.ModeTransactionAdapter
import java.util.Calendar

class TransactionFragment : Fragment(R.layout.fragment_transaction) , DatePickerDialog.OnDateSetListener {

    private val firebaseAuth=FirebaseAuth.getInstance()
    private val  uniqueID = firebaseAuth.currentUser?.uid.toString()



    private lateinit var  transactionsRef : CollectionReference
    private lateinit var transactionVM: TransactionVM
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transactionMode:String
    private lateinit var transactionCategory:String
    private lateinit var transactionType:String
    private var transactionAmount :Int = 0
    private var transactionDescription :String =""
    private  var transactionDay:Int =0
    private  var transactionMonth:Int=0
    private  var transactionYear:Int=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_transaction,container,false)

        val refString = "TRACKEXP/USERS/USERRECORDS/$uniqueID/RECORDS"
        transactionsRef = Firebase.firestore.collection(refString)
        transactionVM = ViewModelProvider(this)[TransactionVM::class.java]

        //spinners
        val typesArray=resources.getStringArray(R.array.type_transaction)
        val modesArray=resources.getStringArray(R.array.mode_transaction)
        val categoriesArrayExpense=resources.getStringArray(R.array.category_transaction)
        val categoriesArrayIncome=resources.getStringArray(R.array.category_transaction_1)

        val typeAdapter= TypeTransactionAdapter(typesArray,requireContext())
        val modeAdapter= ModeTransactionAdapter(modesArray,requireContext())
        val categoryTransactionAdapterExpense= CategoryTransactionAdapter(categoriesArrayExpense,requireContext())
        val categoryTransactionAdapterIncome= CategoryTransactionAdapter(categoriesArrayIncome,requireContext())

        binding.spnTypeOfTransaction.adapter = typeAdapter
        binding.spnModeOfTransaction.adapter = modeAdapter
        binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterIncome

        initTransactionDate()


        binding.ivDatePicker.setOnClickListener{
            openDatePicker()
        }
        binding.tvDateOfTransaction.setOnClickListener {
            openDatePicker()
        }
        binding.spnTypeOfTransaction.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                transactionType= adapterView?.getItemAtPosition(position).toString()
                if(transactionType == "Income"){
                    binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterIncome
                   // binding.textView7.isVisible=false

                }else{
                    binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterExpense
                    //binding.textView7.isVisible=true
                }

            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                transactionType= adapterView?.getItemAtPosition(0).toString()

            }
        }
        binding.spnModeOfTransaction.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                transactionMode= adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                transactionMode= adapterView?.getItemAtPosition(0).toString()
            }
        }
        binding.spnCategoryOfTransaction.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                transactionCategory= adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                transactionCategory= adapterView?.getItemAtPosition(0).toString()

            }
        }




        binding.btnSave.setOnClickListener {
            transactionDescription = binding.etTransactionDescription.text.toString()
            if(binding.etAmount.text.toString() !=""){
                transactionAmount = binding.etAmount.text.toString().toInt()
            }
            if(isValidEntries()){

                val transactionRecord = TransactionRecord(transactionAmount,
                transactionType,transactionCategory,transactionMode,
                transactionDay,transactionMonth,transactionYear,
                transactionDescription)

                val loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
                loadingDialog.startLoginLoadingDialog()

                CoroutineScope(Dispatchers.IO).launch {
                    val result = transactionVM.addTransaction(transactionRecord)
                    if(result){
                        withContext(Dispatchers.Main){
                            loadingDialog.stopLoginLoadingDialog()
                            Toast.makeText(requireContext(),"Added successfully...",Toast.LENGTH_SHORT).show()
                            binding.root.findNavController().navigate(R.id.action_transactionFragment_to_homeFragment)
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            loadingDialog.stopLoginLoadingDialog()
                            Toast.makeText(requireContext(),"Failed to add...",Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }else{
                Toast.makeText(requireContext(),"Please Enter Amount...",Toast.LENGTH_SHORT ).show()
            }


        }

        return binding.root
    }
//

    private fun isValidEntries(): Boolean {
        return transactionAmount!=0
    }


    override fun onResume() {

        super.onResume()
    }


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar =Calendar.getInstance()
        val currentYear=calendar.get(Calendar.YEAR)
        val currentMonth=calendar.get(Calendar.MONTH)
        if( (year != currentYear) ||  (month != currentMonth) ){
            Toast.makeText(requireContext(),"Select Transaction of Current Month/Year",Toast.LENGTH_SHORT).show()
        }else{
            binding.tvDateOfTransaction.text = "$day ${month+1} $year"
            transactionDay=day
            transactionMonth =month+1
            transactionYear =year
        }
    }
    private fun openDatePicker(){
        val newDatePickerFragment = DateDialogClass(this)
        newDatePickerFragment.show(childFragmentManager,"date_picker")
    }
    private fun initTransactionDate() {
        val calendar =Calendar.getInstance()
        val currentYear=calendar.get(Calendar.YEAR)
        val currentDay=calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth=calendar.get(Calendar.MONTH)
        transactionDay=currentDay
        transactionMonth=currentMonth+1
        transactionYear=currentYear
        binding.tvDateOfTransaction.text = "$currentDay ${currentMonth+1} $currentYear"
    }

}
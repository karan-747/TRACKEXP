package com.example.trackexp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.trackexp.databinding.FragmentEditTransactionBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.ModeTransactionAdapter
import java.util.Calendar

class EditTransactionFragment : Fragment(R.layout.fragment_edit_transaction) ,DatePickerDialog.OnDateSetListener{



    private lateinit var editTransactionVM: EditTransactionVM


    private lateinit var categoryTransactionAdapterExpense: CategoryTransactionAdapter
    private lateinit var categoryTransactionAdapterIncome: CategoryTransactionAdapter
        //for old record
    private lateinit var transactionMode:String
    private lateinit var transactionCategory:String
    private lateinit var transactionType:String
    private  lateinit var  binding:FragmentEditTransactionBinding
    private var transactionAmount :Int = 0
    private var transactionDescription :String =""
    private  var transactionDay:Int =0
    private  var transactionMonth:Int=0
    private  var transactionYear:Int=0

        //for new record
    private  var newTransactionMode:String =""
    private  var newTransactionCategory:String= ""
    private  var newTransactionType:String = ""
    private var newTransactionAmount :Int = 0
    private var newTransactionDescription :String =""
    private  var newTransactionDay:Int =0
    private  var newTransactionMonth:Int=0
    private  var newTransactionYear:Int=0
    private  var isFromHome = false
    private var catSelection :Int =0


    private var loadingDialog:LoginLoadingDialog? =null



    private  val transactionTypeMap =  mapOf<String,Int>( "Income" to 0 , "Expense" to 1)
    private  val transactionModeMap =  mapOf<String,Int>("Online" to 0 ,"Cash" to 1)
    private  val transactionCategoryMapExpense = mapOf<String,Int>("Education" to 0,"Food" to 1,"Grocery" to 2,"House Rent" to 3,"Internet" to 4,"Investment" to 5,"Shopping" to 6,"Travel" to 7,"Others" to 8)
    private  val transactionCategoryMapIncome = mapOf<String,Int>("Wages" to 0,"Salary" to 1,"Commission" to 2,"Interest" to 3,"Investments" to 4,"Gifts" to 5,"Pocket Money" to 6,"Government Payments" to 7,"Others" to 8)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_transaction,container,false)

        editTransactionVM = ViewModelProvider(this)[EditTransactionVM::class.java]








        //spinners
        val typesArray=resources.getStringArray(R.array.type_transaction)
        val modesArray=resources.getStringArray(R.array.mode_transaction)
        val categoriesArrayExpense=resources.getStringArray(R.array.category_transaction)
        val categoriesArrayIncome=resources.getStringArray(R.array.category_transaction_1)

        val typeAdapter= TypeTransactionAdapter(typesArray,requireContext())
        val modeAdapter= ModeTransactionAdapter(modesArray,requireContext())
         categoryTransactionAdapterExpense= CategoryTransactionAdapter(categoriesArrayExpense,requireContext())
         categoryTransactionAdapterIncome= CategoryTransactionAdapter(categoriesArrayIncome,requireContext())

        binding.spnTypeOfTransaction.adapter = typeAdapter
        binding.spnModeOfTransaction.adapter = modeAdapter
            //binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterExpense


        binding.spnTypeOfTransaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                newTransactionType = adapterView?.getItemAtPosition(position).toString()
                if(newTransactionType == "Income"){
                    binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterIncome


                }else{
                    binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterExpense

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

        }


        binding.spnCategoryOfTransaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                newTransactionCategory = adapterView?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                //binding.spnCategoryOfTransaction.setSelection(catSelection)

            }

        }

        binding.spnModeOfTransaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                newTransactionMode =  adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

        }

        val receivedArray=arguments?.getStringArray("RECORD")
        val jsonRecord= receivedArray?.get(1)
         isFromHome = receivedArray?.get(0) == "yes"
        jsonRecord?.let {
            val record = Gson().fromJson(jsonRecord,TransactionRecord::class.java)
            if(record!=null){
                initValues(record)
            }
        }





        binding.ivDatePicker.setOnClickListener{
            openDatePicker()
        }
        binding.tvDateOfTransaction.setOnClickListener {
            openDatePicker()
        }
        binding.btnSave.setOnClickListener {

            val updateDialog = AlertDialog.Builder(requireContext()).setCancelable(true)
                .setTitle("Update record")
                .setMessage("Are you sure you want to update record?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener(){
                        dialogInterface, i ->
                    loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
                    loadingDialog?.startLoginLoadingDialog()
                    updateTheRecord()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->

                }).create()

            updateDialog.show()


        }
        binding.ivDelete.setOnClickListener{




            val deleteDialog = AlertDialog.Builder(requireContext()).setCancelable(true)
                .setTitle("Delete record")
                .setMessage("Are you sure you want to delete record??")
                .setPositiveButton("Yes", DialogInterface.OnClickListener(){
                        dialogInterface, i ->
                     loadingDialog = LoginLoadingDialog(requireContext(),binding.root as ViewGroup)
                    loadingDialog?.startLoginLoadingDialog()
                        deleteTherecord()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->

                }).create()

            deleteDialog.show()


        }


        return binding.root


    }

    private fun updateTheRecord() {
        if(binding.etAmount.text.toString().isNotEmpty()){
            newTransactionAmount = binding.etAmount.text.toString().toInt()
        }else{
            newTransactionAmount = transactionAmount
        }
        newTransactionDescription = binding.etTransactionDescription.text.toString()
        val updateMap = makeMap(getNewTransactionRecord())
        CoroutineScope(Dispatchers.IO).launch {
            val result = editTransactionVM.updateTransactionRecord(getOldTransactionRecord(),updateMap)!!
            if(result){
                withContext(Dispatchers.Main){
                    loadingDialog?.stopLoginLoadingDialog()
                    Toast.makeText(requireContext(),"Updated successfully...",Toast.LENGTH_SHORT).show()
                    if(isFromHome){
                        binding.root.findNavController().navigate(R.id.action_editTransactionFragment_to_homeFragment)
                    }else{
                        binding.root.findNavController().navigate(R.id.action_editTransactionFragment_to_transactionHistoryFragment)
                    }

                }
            }else{
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(),"Failed to update...",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun deleteTherecord(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = editTransactionVM.deleteTransactionRecord(getOldTransactionRecord())
            if(result){
                withContext(Dispatchers.Main){
                    loadingDialog?.stopLoginLoadingDialog()
                    Toast.makeText(requireContext(),"Deleted...",Toast.LENGTH_SHORT).show()

                    if(isFromHome){
                        binding.root.findNavController()
                            .navigate(R.id.action_editTransactionFragment_to_homeFragment)
                    }else {
                        binding.root.findNavController()
                            .navigate(R.id.action_editTransactionFragment_to_transactionHistoryFragment)
                    }
                }

            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(requireContext(),"Failed to delete...",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if(transactionType == "Income") {

                binding.spnCategoryOfTransaction.setSelection(4)
                catSelection= catSelection
                //Toast.makeText(requireContext(), "$catSelection $transactionCategory", Toast.LENGTH_SHORT)
                  //  .show()

        }
        else {

                binding.spnCategoryOfTransaction.setSelection(4)

//                Toast.makeText(requireContext(), "$catSelection $transactionCategory", Toast.LENGTH_SHORT)
//                    .show()

        }
    }



    private fun initValues(record:TransactionRecord){
        transactionType = record.type
        if(transactionType == "Income"){
            binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterIncome
        }else{
            binding.spnCategoryOfTransaction.adapter = categoryTransactionAdapterExpense
        }

        transactionCategory =record.category
        transactionMode =record.mode
        transactionDay = record.day
        transactionMonth = record.month
        transactionYear = record.year
        transactionDescription = record.description
        transactionAmount = record.amount
        binding.etAmount.setText(record.amount.toString())
        binding.tvDateOfTransaction.text = "${record.day}/${record.month}/${record.year}"
        binding.etTransactionDescription.setText(record.description)

        transactionTypeMap[transactionType]?.let { it1 ->
            binding.spnTypeOfTransaction.setSelection(it1,true)

        }

        if(transactionType == "Income") {
            transactionCategoryMapIncome[transactionCategory]?.let {
                //binding.spnCategoryOfTransaction.setSelection(it)
                catSelection= it
//                Toast.makeText(requireContext(), "$it $transactionCategory", Toast.LENGTH_SHORT)
//                    .show()
            }
        }
        else {
            transactionCategoryMapExpense[transactionCategory]?.let {
                //binding.spnCategoryOfTransaction.setSelection(it)
                catSelection = it
//                Toast.makeText(requireContext(), "$it $transactionCategory", Toast.LENGTH_SHORT)
//                    .show()
            }
        }

        transactionModeMap[transactionMode]?.let {
            binding.spnModeOfTransaction.setSelection(it,true)
        }

        newTransactionType = transactionType
        newTransactionMode = transactionMode
        newTransactionCategory = transactionCategory

        newTransactionDay = transactionDay
        newTransactionMonth = transactionMonth
        newTransactionYear = transactionYear
        newTransactionDescription = transactionDescription




    }


    private fun openDatePicker(){
        val newDatePickerFragment = DateDialogClass(this)
        newDatePickerFragment.show(childFragmentManager,"date_picker")
    }

    private fun makeMap(newRecord: TransactionRecord):Map<String,Any>{
        val updateMap = mutableMapOf<String,Any>()
        updateMap["amount"]= newRecord.amount
        updateMap["type"]= newRecord.type
        updateMap["category"]= newRecord.category
        updateMap["mode"]= newRecord.mode
        updateMap["day"]= newRecord.day
        updateMap["month"]= newRecord.month
        updateMap["year"]= newRecord.year
        updateMap["description"]= newRecord.description
        return updateMap
    }

    private fun getNewTransactionRecord():TransactionRecord{
        return TransactionRecord(newTransactionAmount,newTransactionType,newTransactionCategory,newTransactionMode,newTransactionDay,newTransactionMonth,newTransactionYear,newTransactionDescription)
    }


    private fun getOldTransactionRecord():TransactionRecord{
        return TransactionRecord(transactionAmount,transactionType,transactionCategory,transactionMode,transactionDay,transactionMonth,transactionYear,transactionDescription)
    }


    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar =Calendar.getInstance()
        val currentYear=calendar.get(Calendar.YEAR)
        val currentMonth=calendar.get(Calendar.MONTH)
        if( (year != currentYear) ||  (month != currentMonth) ){
            Toast.makeText(requireContext(),"Select Transaction of Current Month/Year", Toast.LENGTH_SHORT).show()
        }else{
            binding.tvDateOfTransaction.text = "$day ${month+1} $year"
            newTransactionDay=day
            newTransactionMonth =month+1
            newTransactionYear =year
        }
    }


}
package com.example.trackexp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trackexp.databinding.AnalysisItemBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.Calendar

class AnalysisItemRVAdapter:RecyclerView.Adapter<AnalysisItemRVAdapter.AnalysisVH>() {
    private var dataList  = ArrayList<ArrayList<PieEntry>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisVH {
       val binding = DataBindingUtil.inflate<AnalysisItemBinding>(LayoutInflater.from(parent.context),R.layout.analysis_item,parent,false)
        return AnalysisVH(binding){
            Toast.makeText(parent.context,"Image saved in internal storage",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AnalysisVH, position: Int) {
        when (position){

            0->{
                holder.bind0(dataList[position])
            }
            1->{
                holder.bind01(dataList[position])
            }
            2->{
                holder.bind1(dataList[position])
            }
            3->{
                holder.bind2(dataList[position])
            }
        }
    }


    fun updateList(list:ArrayList<ArrayList<PieEntry>>){
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()


    }




    inner class AnalysisVH(val binding: AnalysisItemBinding,private val showToast: ()->Unit) :RecyclerView.ViewHolder(binding.root){

        //income mode

        private val calender =Calendar.getInstance()
        private val monthFormat = SimpleDateFormat("LLLL")
        val monthName = monthFormat.format(calender.time)
        private val currentYear = calender.get(Calendar.YEAR)
        val mCenterText = "$monthName, $currentYear"
        fun bind0(recordList :ArrayList<PieEntry>){
            binding.tvTitile.text = "Income vs Mode"



            var check= (recordList[0].value == 0f) && (recordList[1].value == 0f)
            if(check){
                binding.tvNoTransactionsRecord.visibility =View.VISIBLE
                binding.pieChart.visibility = View.GONE
            }else{
                binding.tvNoTransactionsRecord.visibility =View.GONE
                binding.pieChart.visibility = View.VISIBLE
            }



            val pieDataSet = PieDataSet(recordList,"Income-Mode Chart")

            pieDataSet.apply {
                setDrawValues(true)
                setColors(ColorTemplate.MATERIAL_COLORS,255)
                sliceSpace = 2f
                valueTextSize = 10f
            }
            val pieData = PieData(pieDataSet)
            binding.pieChart.apply {
                legend.form = Legend.LegendForm.CIRCLE
                legend.isEnabled = false
                description.isEnabled = true
                description.text = "Income-Mode Chart"
                isRotationEnabled = true
                centerText = mCenterText
                setCenterTextColor(R.color.black)

                transparentCircleRadius = 0f
                holeRadius = 50f
                setDrawEntryLabels(true)
                setEntryLabelTextSize(10f)
                data = pieData
                animateY(1400, Easing.EaseInOutQuad)
                setNoDataText("No transactions record available")
                invalidate()
            }
            binding.pieChart.setOnChartValueSelectedListener( object: OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {

                }

                override fun onNothingSelected() {

                }
            })
            binding.ivDownload.setOnClickListener {
                binding.pieChart.saveToGallery("income_vs_mode")
                showToast.invoke()
            }

        }

        //expense mode
        fun bind1(recordList :ArrayList<PieEntry>){
            binding.tvTitile.text ="Expense vs Mode"

            var check= (recordList[0].value == 0f) && (recordList[1].value == 0f)

            if(check){
                binding.tvNoTransactionsRecord.visibility =View.VISIBLE
                binding.pieChart.visibility = View.GONE
            }else{
                binding.tvNoTransactionsRecord.visibility =View.GONE
                binding.pieChart.visibility = View.VISIBLE
            }

            val pieDataSet = PieDataSet(recordList,"Expense-Mode Chart")
            for(i in recordList) {
                Log.d("TAGY1", i.y.toString())
            }
            pieDataSet.apply {
                setDrawValues(true)
                setColors(ColorTemplate.MATERIAL_COLORS,255)
                sliceSpace = 2f
                valueTextSize = 10f
            }
            val pieData = PieData(pieDataSet)
            binding.pieChart.apply {
                legend.form = Legend.LegendForm.CIRCLE
                legend.isEnabled = false
                description.isEnabled = true
                description.text = "Expense-mode Chart"
                isRotationEnabled = true
                transparentCircleRadius = 0f
                holeRadius = 50f
                centerText = mCenterText
                setCenterTextColor(R.color.black)
                setDrawEntryLabels(true)
                setNoDataText("No transactions record available")
                setEntryLabelTextSize(10f)
                data = pieData
                animateY(1400, Easing.EaseInOutQuad)
                invalidate()
            }
            binding.pieChart.setOnChartValueSelectedListener( object: OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {

                }

                override fun onNothingSelected() {

                }
            })
            binding.ivDownload.setOnClickListener {
                binding.pieChart.saveToGallery("expense_vs_mode")
                showToast.invoke()
            }


        }

        //expense category
        fun bind2(recordList :ArrayList<PieEntry>){
            binding.tvTitile.text = "Expense vs Category"


            var check= true
            for(i in recordList){
                check = check && (i.value == 0f)
            }

            if(check){
                binding.tvNoTransactionsRecord.visibility =View.VISIBLE
                binding.pieChart.visibility = View.GONE
            }else{
                binding.tvNoTransactionsRecord.visibility =View.GONE
                binding.pieChart.visibility = View.VISIBLE
            }

            val pieDataSet = PieDataSet(recordList,"Expense-Category Chart")

            pieDataSet.apply {
                setDrawValues(true)
                setColors(ColorTemplate.MATERIAL_COLORS,255)
                sliceSpace = 2f
                valueTextSize = 10f
            }
            val pieData = PieData(pieDataSet)
            binding.pieChart.apply {
                legend.form = Legend.LegendForm.CIRCLE
                legend.isEnabled = false
                description.isEnabled = true
                description.text = "Expense-Category Chart"
                isRotationEnabled = true
                transparentCircleRadius = 0f
                holeRadius = 50f
                centerText = mCenterText
                setCenterTextColor(R.color.black)
                setNoDataText("No transactions record available")
                setDrawEntryLabels(true)
                setEntryLabelTextSize(10f)
                data = pieData
                animateY(1400, Easing.EaseInOutQuad)
                invalidate()
            }
            binding.pieChart.setOnChartValueSelectedListener( object: OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {

                }

                override fun onNothingSelected() {

                }
            })
            binding.ivDownload.setOnClickListener {
                binding.pieChart.saveToGallery("expense_vs_category")
                showToast.invoke()
            }

        }

        fun bind01(recordList: ArrayList<PieEntry>) {
            binding.tvTitile.text = "Income vs Category"


            var check= true
            for(i in recordList){
                check = check && (i.value == 0f)
            }

            if(check){
                binding.tvNoTransactionsRecord.visibility =View.VISIBLE
                binding.pieChart.visibility = View.GONE
            }else{
                binding.tvNoTransactionsRecord.visibility =View.GONE
                binding.pieChart.visibility = View.VISIBLE
            }

            val pieDataSet = PieDataSet(recordList,"Income-Category Chart")

            pieDataSet.apply {
                setDrawValues(true)
                setColors(ColorTemplate.MATERIAL_COLORS,255)
                sliceSpace = 2f
                valueTextSize = 10f
            }

            val pieData = PieData(pieDataSet)
            binding.pieChart.apply {
                legend.form = Legend.LegendForm.CIRCLE
                legend.isEnabled = false
                description.isEnabled = true
                description.text = "Income-Category Chart"
                isRotationEnabled = true
                transparentCircleRadius = 0f
                holeRadius = 50f
                centerText = mCenterText
                setCenterTextColor(R.color.black)
                setNoDataText("No transactions record available")
                setDrawEntryLabels(true)
                setEntryLabelTextSize(10f)
                data = pieData
                animateY(1400, Easing.EaseInOutQuad)
                invalidate()
            }

            binding.pieChart.setOnChartValueSelectedListener( object: OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                }
                override fun onNothingSelected() {
                }
            })

            binding.ivDownload.setOnClickListener {
                binding.pieChart.saveToGallery("income_vs_category")
                showToast.invoke()
            }

        }
    }
}
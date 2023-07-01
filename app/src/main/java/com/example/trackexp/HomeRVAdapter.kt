package com.example.trackexp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class HomeRVAdapter(private val myContext : Context,private val onRecordClicked:(TransactionRecord)->Unit):RecyclerView.Adapter<HomeRVAdapter.HomeRVViewHolder>() {

    private val records = ArrayList<TransactionRecord>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_record_item,parent,false)
        return HomeRVViewHolder(view)

    }



    override fun getItemCount(): Int {
        if(records.size <= 5){
            return records.size
        }
        return 5
    }

    override fun onBindViewHolder(holder: HomeRVViewHolder, position: Int) {
        holder.bind(records[position])
    }

     fun updateList(newRecords: java.util.ArrayList<TransactionRecord>) {
        records.clear()
        records.addAll(newRecords)
        notifyDataSetChanged()
    }




    inner class HomeRVViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(record:TransactionRecord){
            val tvTransAmnt = view.findViewById<TextView>(R.id.tvTransAmnt)
            val tvTransCat = view.findViewById<TextView>(R.id.tvTransCat)
            val tvTransDate = view.findViewById<TextView>(R.id.tvTransDate)
            val tvTransMode = view.findViewById<TextView>(R.id.tvTransMode)
            val ivTransRecImg = view.findViewById<ImageView>(R.id.ivTransRecImg)
            if(record.type == "Income"){
                tvTransAmnt.text= "+ ₹${record.amount}"
                tvTransAmnt.setTextColor(ContextCompat.getColor(myContext, R.color.my_color_3))
                tvTransCat.text = record.category
                tvTransDate.text = "${record.day}/${record.month}/${record.year}"
                tvTransMode.text = record.mode
                //ivTransRecImg.setImageResource(R.drawable.ic_money_in)
            }else{
                tvTransAmnt.text= "- ₹${record.amount}"
                //.setTextColor(ContextCompat.getColor(myContext, R.color.my_color_3))
                tvTransCat.text = record.category
                tvTransDate.text = "${record.day}/${record.month}/${record.year}"
                tvTransMode.text = record.mode



            }

            when(record.category){
                "Investment"->{
                    ivTransRecImg.setImageResource(R.drawable.bitcoin_logo)
                }
                "Education"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_books)
                }
                "Food"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_dish)
                }
                "Grocery"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_grocery)
                }
                "House Rent"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_house_rent)
                }
                "Internet"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_internet)
                }
                "Shopping"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_shopping)
                }
                "Travel"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_travel)
                }
                "Others"->{
                    ivTransRecImg.setImageResource(R.drawable.ic_others)
                }

                "Wages"->{
                    ivTransRecImg.setImageResource(R.drawable.payment)
                }
                "Salary"->{
                    ivTransRecImg.setImageResource(R.drawable.salary)
                }

                "Commission"->{
                    ivTransRecImg.setImageResource(R.drawable.discount)
                }
                "Interest"->{
                    ivTransRecImg.setImageResource(R.drawable.loan)
                }

                "Investments"->{
                    ivTransRecImg.setImageResource(R.drawable.bitcoin_logo)
                }
                "Gifts"->{
                    ivTransRecImg.setImageResource(R.drawable.birthday_card)
                }
                "Government Payments"->{
                    ivTransRecImg.setImageResource(R.drawable.grant)
                }
                "Pocket Money"->{
                    ivTransRecImg.setImageResource(R.drawable.wallet)
                }
            }

            view.setOnClickListener {
                onRecordClicked(record)
            }
        }
    }
}
package com.example.trackexp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class TypeTransactionAdapter(private val Types:Array<String>,private val parentContext:Context):BaseAdapter() {




    override fun getCount(): Int {
        return Types.size
    }

    override fun getItem(p0: Int): Any {
        return Types[p0]
    }

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val rootView = LayoutInflater.from(parentContext).inflate(R.layout.drop_down_item,parent,false)
        val tvType= rootView.findViewById<TextView>(R.id.tvDropDownItem)
        val ivType= rootView.findViewById<ImageView>(R.id.ivDropDownItem)
        tvType.text=Types[position]
        when(tvType.text.toString()){
            "Income"->{
                ivType.setImageResource(R.drawable.ic_money_in)
            }
            "Expense"->{
                ivType.setImageResource(R.drawable.ic_money_out)
            }
        }

        return rootView
    }
}
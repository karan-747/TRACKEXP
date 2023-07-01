package com.example.trackexp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CategoryTransactionAdapter  (private val categories:Array<String>,private val parentContext: Context): BaseAdapter()
{
    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(p0: Int): Any {
        return categories[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val rootView=LayoutInflater.from(parentContext).inflate(R.layout.drop_down_item,parent,false)
        val tvCategory= rootView.findViewById<TextView>(R.id.tvDropDownItem)
        val ivCategory= rootView.findViewById<ImageView>(R.id.ivDropDownItem)

        tvCategory.text=categories[position]
        when(categories[position]){
            "Education"->{
                ivCategory.setImageResource(R.drawable.ic_books)
            }
            "Food"->{
                ivCategory.setImageResource(R.drawable.ic_dish)
            }
            "Grocery"->{
                ivCategory.setImageResource(R.drawable.ic_grocery)
            }
            "House Rent"->{
                ivCategory.setImageResource(R.drawable.ic_house_rent)
            }
            "Internet"->{
                ivCategory.setImageResource(R.drawable.ic_internet)
            }
            "Investment"->{
                ivCategory.setImageResource(R.drawable.bitcoin_logo)
            }
            "Shopping"->{
                ivCategory.setImageResource(R.drawable.ic_shopping)
            }
            "Travel"->{
                ivCategory.setImageResource(R.drawable.ic_travel)
            }
            "Others"->{
                ivCategory.setImageResource(R.drawable.ic_others)
            }

            "Wages"->{
                ivCategory.setImageResource(R.drawable.payment)
            }
            "Salary"->{
                ivCategory.setImageResource(R.drawable.salary)
            }

            "Commission"->{
                ivCategory.setImageResource(R.drawable.discount)
            }
            "Interest"->{
                ivCategory.setImageResource(R.drawable.loan)
            }

            "Investments"->{
                ivCategory.setImageResource(R.drawable.bitcoin_logo)
            }
            "Gifts"->{
                ivCategory.setImageResource(R.drawable.birthday_card)
            }
            "Government Payments"->{
                ivCategory.setImageResource(R.drawable.grant)
            }
            "Pocket Money"->{
                ivCategory.setImageResource(R.drawable.wallet)
            }

        }
        return rootView
    }


}
package layout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.trackexp.R

class ModeTransactionAdapter (private val modeArray:Array<String>,private val parentContext: Context):BaseAdapter(){
    override fun getCount(): Int {
        return modeArray.size
    }

    override fun getItem(position: Int): Any {
       return modeArray[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val rootView= LayoutInflater.from(parentContext).inflate(R.layout.drop_down_item,parent,false)
        val tvMode=rootView.findViewById<TextView>(R.id.tvDropDownItem)
        val ivMode=rootView.findViewById<ImageView>(R.id.ivDropDownItem)

        tvMode.text = modeArray[position]
        when(tvMode.text.toString()){

            "Online"->{
                ivMode.setImageResource(R.drawable.ic_online)
            }
            "Cash"->{
                ivMode.setImageResource(R.drawable.ic_offline)
            }

        }

        return rootView
    }

}
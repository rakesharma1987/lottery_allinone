package com.lottery.lotteryallinone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lottery.lotteryallinone.R
import com.pick3andpick4pred.suvarnatechlabs.model.FourNumbers

class FourPredictorAdapter(private val fourNumbersList : List<FourNumbers>) : RecyclerView.Adapter<FourPredictorAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.layout_four_predict, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val numbers : FourNumbers? = fourNumbersList[position]
        holder.tvSNo.text = (position.plus(1)).toString()
        holder.tvNum1.text = numbers!!.num1.toString()
        holder.tvNum2.text = numbers!!.num2.toString()
        holder.tvNum3.text = numbers!!.num3.toString()
        holder.tvNum4.text = numbers!!.num4.toString()
    }

    override fun getItemCount(): Int {
        return fourNumbersList.size
    }

    public class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvNum1 : TextView = itemView.findViewById(R.id.tv1)
        val tvNum2 : TextView = itemView.findViewById(R.id.tv2)
        val tvNum3 : TextView = itemView.findViewById(R.id.tv3)
        val tvNum4 : TextView = itemView.findViewById(R.id.tv4)
        val tvSNo : TextView = itemView.findViewById(R.id.tv_sno)
    }
}
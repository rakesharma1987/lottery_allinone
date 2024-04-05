package com.lottery.lotteryallinone.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lottery.lotteryallinone.R
import com.lottery.lotteryallinone.activitys.PowerballActivity
import com.lottery.lotteryallinone.databinding.LayoutLotteryItemsBinding
import com.lottery.lotteryallinone.model.LotteryItem

class RVAdapter(private var items: List<LotteryItem>, private var context: Context): RecyclerView.Adapter<RVAdapter.MyViewHolder>()  {
    inner class MyViewHolder(val itemDashboardItemBinding: LayoutLotteryItemsBinding?): RecyclerView.ViewHolder(itemDashboardItemBinding!!.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var myViewHolder: LayoutLotteryItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.layout_lottery_items, parent, false)
        return MyViewHolder(myViewHolder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemDashboardItemBinding!!.tvName.text = items[position].name
        holder.itemDashboardItemBinding.iv.setImageDrawable(items[position].icon)
        holder.itemView.setOnClickListener {
            when(position){
                0 ->{

                }
                1 ->{

                }
                2 ->{

                }
                3 ->{
                    context.startActivity(Intent(context, PowerballActivity::class.java))
                }
                4 ->{

                }
            }
        }
    }
}
package com.example.coin_app.view.adapter

import android.content.Context
import android.media.Image
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.coin_app.R
import com.example.coin_app.db.entity.InterestCoinEntity
import org.w3c.dom.Text

class CoinListRVAdapter(val context: Context, private val dataSet: List<InterestCoinEntity>) :
    RecyclerView.Adapter<CoinListRVAdapter.ViewHolder>() {
    interface ItemClick{
        fun onClick(view:View,position: Int)
    }
    var itemClick : ItemClick? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coinName: TextView = view.findViewById(R.id.coinName)
        val likeBtn: ImageView = view.findViewById(R.id.likeBtn)
        val upDown : TextView = view.findViewById(R.id.coinPriceUpDown)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.intro_coin_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinListRVAdapter.ViewHolder, position: Int) {
        holder.coinName.text = dataSet[position].coin_name

        holder.upDown.text = dataSet[position].fluctate_rate_24H
        if(dataSet[position].fluctate_rate_24H.toDouble()<0){
            holder.upDown.setTextColor(ContextCompat.getColor(context, R.color.blue))
        }else if(dataSet[position].fluctate_rate_24H.toDouble()>0) {
            holder.upDown.setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.upDown.text = "+"+dataSet[position].fluctate_rate_24H
        }else{
            holder.upDown.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        val selected = dataSet[position].selected
        holder.itemView.findViewById<ImageView>(R.id.likeBtn).setOnClickListener { v->
            itemClick?.onClick(v,position)
        }
        if(selected){
            holder.likeBtn.setImageResource(R.drawable.like_red)
        }else{
            holder.likeBtn.setImageResource(R.drawable.like_grey)

        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
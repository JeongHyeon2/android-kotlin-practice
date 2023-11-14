package com.example.coin_app.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.coin_app.R
import com.example.coin_app.dataModel.CurrentPriceResult
import timber.log.Timber

class SelectRVAdapter(val context : Context, private val coinPriceList : List<CurrentPriceResult> ) : RecyclerView.Adapter<SelectRVAdapter.ViewHolder>(){
    private val selectedCoinList = ArrayList<String>()
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val coinName : TextView = view.findViewById(R.id.coinName)
        val coinPriceUpDown : TextView = view.findViewById(R.id.coinPriceUpDown)
        val likeImage : ImageView = view.findViewById(R.id.likeBtn)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.intro_coin_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coinPriceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.coinName.text = coinPriceList[position].coinName
        val upDown =  holder.coinPriceUpDown


        if(coinPriceList[position].coinInfo.fluctate_rate_24H.toDouble()<0){
            upDown.setTextColor(ContextCompat.getColor(context, R.color.blue))
        }else if(coinPriceList[position].coinInfo.fluctate_rate_24H.toDouble()>0) {
            upDown.setTextColor(ContextCompat.getColor(context, R.color.red))
        }else{
            upDown.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        upDown.text = (coinPriceList[position].coinInfo.fluctate_rate_24H +"%")

        val likeImage = holder.likeImage
        val currentCoin = coinPriceList[position].coinName
        // view가 그려질 때
        if(selectedCoinList.contains(currentCoin)){
            likeImage.setImageResource(R.drawable.like_red)
        }else{
            likeImage.setImageResource(R.drawable.like_grey)
        }


        likeImage.setOnClickListener {
            if(selectedCoinList.contains(currentCoin)){
                selectedCoinList.remove(currentCoin)
                likeImage.setImageResource(R.drawable.like_grey)

            }else{
                selectedCoinList.add(currentCoin)
                likeImage.setImageResource(R.drawable.like_red)
            }
        }

    }
}
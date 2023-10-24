package com.example.solo_life_app.contents_list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.solo_life_app.R

class RVAdapter (val context: Context, val item : ArrayList<ContentModel>): RecyclerView.Adapter<RVAdapter.ViewHolder>(){
    interface ItemClick{
        fun onClick(view:View,position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RVAdapter.ViewHolder, position: Int) {
        if(itemClick!=null){
            holder.itemView.setOnClickListener { v->itemClick?.onClick(v,position) }
        }
        holder.bindItems(item[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(item:ContentModel){
            val iv = itemView.findViewById<ImageView>(R.id.image_view)
            val text = itemView.findViewById<TextView>(R.id.tv)

            Glide.with(context).load(item.imgUrl).into(iv)
            text.text = item.title

        }

    }

}
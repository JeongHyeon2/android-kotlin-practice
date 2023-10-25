package com.example.solo_life_app.contents_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.solo_life_app.R

class RVAdapter (val context: Context, val item : ArrayList<ContentModel>): RecyclerView.Adapter<RVAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RVAdapter.ViewHolder, position: Int) {

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
            val bookmark = itemView.findViewById<ImageView>(R.id.bookmark_view)

            bookmark.setOnClickListener { Toast.makeText(context,"북마크 클릭",Toast.LENGTH_SHORT).show() }

            Glide.with(context).load(item.imgUrl).into(iv)
            text.text = item.title
            itemView.setOnClickListener {
                Toast.makeText(context,text.text.toString(),Toast.LENGTH_SHORT).show()
                val intent = Intent(context,ContentShowActivity::class.java)
                intent.putExtra("url",item.webUrl)
                itemView.context.startActivity(intent)
            }


        }

    }

}
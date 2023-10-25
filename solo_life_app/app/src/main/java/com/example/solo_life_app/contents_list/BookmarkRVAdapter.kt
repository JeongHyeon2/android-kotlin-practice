package com.example.solo_life_app.contents_list
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.solo_life_app.R
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef

class BookmarkRVAdapter(
    val context: Context,
    val item: ArrayList<ContentModel>,
    val keyList: ArrayList<String>,
    val bookmarkIdList: MutableList<String>
) : RecyclerView.Adapter<BookmarkRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkRVAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        Log.d("testlist", bookmarkIdList.toString())

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRVAdapter.ViewHolder, position: Int) {

        holder.bindItems(item[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: ContentModel, key: String) {
            val iv = itemView.findViewById<ImageView>(R.id.image_view)
            val text = itemView.findViewById<TextView>(R.id.tv)
            val bookmark = itemView.findViewById<ImageView>(R.id.bookmark_view)

            if (bookmarkIdList.contains(key)) {
                bookmark.setImageResource(R.drawable.bookmark_color)

            } else {
                bookmark.setImageResource(R.drawable.bookmark_white)

            }

            bookmark.setOnClickListener {
                Toast.makeText(context, key, Toast.LENGTH_SHORT).show()
                if (bookmarkIdList.contains(key)) {

                    FirebaseRef.bookMarkRef.child(FirebaseAuth.getUid()).child(key)
                        .removeValue()

                } else {
                    FirebaseRef.bookMarkRef.child(FirebaseAuth.getUid()).child(key)
                        .setValue(BookmarkModel(true))
                }
            }

            Glide.with(context).load(item.imgUrl).into(iv)
            text.text = item.title
            itemView.setOnClickListener {
                Toast.makeText(context, text.text.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }


        }

    }

}
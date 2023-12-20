package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.models.RecipeModelWithId
import com.example.cooking_app.room.MyDatabase
import com.example.cooking_app.utils.App
import com.example.cooking_app.utils.FBAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyRecipeRVAdapter : RecyclerView.Adapter<MyRecipeRVAdapter.ViewHolder>() {
    private var recipeList: List<RecipeModelWithId> = emptyList()
    private var itemClickListener: ((position: Int) -> Unit)? = null
    private var longItemClickListener: ((position: Int) -> Unit)? = null
    private val db = MyDatabase.getDatabase(App.context())


    fun submitList(newList: List<RecipeModelWithId>) {
        recipeList = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        itemClickListener = listener
    }

    fun setOnLongItemClickListener(listener: (position: Int) -> Unit) {
        longItemClickListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.my_recipe_rv_item_title)
        private val iv: ImageView = view.findViewById(R.id.my_recipe_rv_item_iv)
        private val background: LinearLayout = view.findViewById(R.id.my_recipe_rv_item_background)

        fun bind(item: RecipeModelWithId, position: Int) {
            if (item.model.image != "") {
//                val storageRef = Firebase.storage.reference.child("${item.model.image}")
//                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Glide.with(App.context()).load(task.result)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .apply(RequestOptions.bitmapTransform(RoundedCorners(80)))
//                            .into(iv)
//                    }
//                })
                CoroutineScope(Dispatchers.IO).launch {
                    val image = db.imageDao().getOneData(FBAuth.getUid() + "." + item.id)
                    withContext(Dispatchers.Main) {
                        if(image.image!=null) {
                            iv.setImageBitmap(image.image)
                        }
                    }

                }


            }
            title.text = item.model.title
            title.setOnClickListener { itemClickListener?.invoke(position) }
            title.setOnLongClickListener {
                longItemClickListener?.invoke(position)
                true
            }

            iv.setOnClickListener { itemClickListener?.invoke(position) }
            iv.setOnLongClickListener {
                longItemClickListener?.invoke(position)
                true
            }
            background.setOnClickListener { itemClickListener?.invoke(position) }
            background.setOnLongClickListener {
                longItemClickListener?.invoke(position)
                true // Return true to consume the long click event
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_recipe_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position], position)

    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}

package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeModel

class MyRecipeRVAdapter : RecyclerView.Adapter<MyRecipeRVAdapter.ViewHolder>() {
    private var recipeList: List<RecipeModel> = emptyList()
    private var itemClickListener: ((position: Int) -> Unit)? = null


    fun submitList(newList: List<RecipeModel>) {
        recipeList = newList
        notifyDataSetChanged()
    }
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.my_recipe_rv_item_title)
        private val iv : ImageView = view.findViewById(R.id.my_recipe_rv_item_iv)


        fun bind(item: RecipeModel,position: Int) {
            title.text = item.title
            if(item.image!=null){
            iv.setImageBitmap(item.image)}
            iv.setOnClickListener { itemClickListener?.invoke(position) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_recipe_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position],position)

    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}
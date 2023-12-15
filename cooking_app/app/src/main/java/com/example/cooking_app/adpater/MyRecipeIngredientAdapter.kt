package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeIngredient


class MyRecipeIngredientAdapter() : RecyclerView.Adapter<MyRecipeIngredientAdapter.ViewHolder>() {
    private var ingredientList: List<RecipeIngredient> =  emptyList()
    private var longItemClickListener: ((position: Int) -> Unit)? = null
    private var itemClickListener: (() -> Unit)? = null

    fun setOnItemClickListener(listener: ()-> Unit) {
        itemClickListener = listener
    }
    fun setOnLongItemClickListener(listener: (position: Int) -> Unit) {
        longItemClickListener = listener
    }
    fun submitList(newList: List<RecipeIngredient>) {
        ingredientList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.ingredient_name)
        private val amount: TextView = view.findViewById(R.id.ingredient_amount)
        private val layout : LinearLayout = view.findViewById(R.id.ingredient_item_linear)


        fun bind(item:RecipeIngredient) {
            name.text = "• "+item.name+" "
            amount.text = item.amount +"g"
            layout.setOnLongClickListener {
                longItemClickListener?.invoke(position)
                true // Return true to consume the long click event
            }
            layout.setOnClickListener {
                itemClickListener?.invoke()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_recipe_ingredient_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientList[position])
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
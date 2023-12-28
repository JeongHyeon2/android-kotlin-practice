package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.models.RecipeIngredientForDB

class IngredientFragmentAdapter : RecyclerView.Adapter<IngredientFragmentAdapter.ViewHolder>() {
    private var ingredients: List<RecipeIngredientForDB> = emptyList()
    private var itemClickListener: ((position: Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        itemClickListener = listener
    }

    fun submitList(newList: List<RecipeIngredientForDB>) {
        ingredients = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.ingredient_fragment_item_name)
        private val cost: TextView = view.findViewById(R.id.ingredient_fragment_item_cost)
        private val amountPurchase: TextView =
            view.findViewById(R.id.ingredient_fragment_item_amount_purchase)
        private val calorie: TextView = view.findViewById(R.id.ingredient_fragment_item_calorie)
        private val ll: LinearLayout = view.findViewById(R.id.ingredient_fragment_item_ll)

        fun bind(ingredient: RecipeIngredientForDB, position: Int) {
            name.text = ingredient.name
            cost.text = if (ingredient.cost.isEmpty()) {
                "0원"
            } else {
                ingredient.cost + "원"
            }
            amountPurchase.text = if (ingredient.amountOfPurchase.isEmpty()) {
                "0g"
            } else {
                ingredient.amountOfPurchase + "g"
            }
            calorie.text = if (ingredient.calorie.isEmpty()) {
                "0kcal"
            } else {
                ingredient.calorie + "kcal"
            }
            name.setOnClickListener { itemClickListener?.invoke(position) }
            cost.setOnClickListener { itemClickListener?.invoke(position) }
            amountPurchase.setOnClickListener { itemClickListener?.invoke(position) }
            calorie.setOnClickListener { itemClickListener?.invoke(position) }
            ll.setOnClickListener { itemClickListener?.invoke(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientFragmentAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredient_fragment_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientFragmentAdapter.ViewHolder, position: Int) {
        holder.bind(ingredients[position], position)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
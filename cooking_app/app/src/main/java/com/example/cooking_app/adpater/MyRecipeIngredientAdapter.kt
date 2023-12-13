package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeIngredient


class MyRecipeIngredientAdapter() : RecyclerView.Adapter<MyRecipeIngredientAdapter.ViewHolder>() {
    private var ingredientList: List<RecipeIngredient> =  emptyList()
    fun submitList(newList: List<RecipeIngredient>) {
        ingredientList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: EditText = view.findViewById(R.id.ingredient_name)
        private val amount: EditText = view.findViewById(R.id.ingredient_amount)
        private val purchase: EditText = view.findViewById(R.id.ingredient_purchase)
        private val purchaseAmount: EditText = view.findViewById(R.id.ingredient_purchase_amount)
        private val calorie: EditText = view.findViewById(R.id.ingredient_calorie)

        fun bind(item:RecipeIngredient) {
            name.setText(item.name)
            amount.setText(item.amount)
            purchase.setText(item.cost)
            purchaseAmount.setText(item.amountOfPurchase)
            calorie.setText(item.calorie)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_recipe_create_rv_item, parent, false)
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
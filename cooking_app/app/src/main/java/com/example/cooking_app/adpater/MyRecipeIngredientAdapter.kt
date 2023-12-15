package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
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

        private val name: TextView = view.findViewById(R.id.ingredient_name)
        private val amount: TextView = view.findViewById(R.id.ingredient_amount)
//        private val purchase: TextView = view.findViewById(R.id.ingredient_purchase)
//        private val purchaseAmount: TextView = view.findViewById(R.id.ingredient_purchase_amount)
//        private val calorie: TextView = view.findViewById(R.id.ingredient_calorie)

        fun bind(item:RecipeIngredient) {
            name.text = "• "+item.name+" "
            amount.text = item.amount +"g"
//            purchase.text = item.cost + "원"
//            purchaseAmount.text = item.amountOfPurchase+"g"
//            calorie.text = item.calorie +"kcal"

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
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


class MyRecipeIngredientInfoAdapter() :
    RecyclerView.Adapter<MyRecipeIngredientInfoAdapter.ViewHolder>() {
    private var ingredientList: List<RecipeIngredient> = emptyList()
    private var itemClickListener: ((position: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        itemClickListener = listener
    }

    fun submitList(newList: List<RecipeIngredient>) {
        ingredientList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: EditText = view.findViewById(R.id.ingredient_name)
        private val amount: EditText = view.findViewById(R.id.ingredient_amount)
        private val purchase: EditText = view.findViewById(R.id.ingredient_purchase)
        private val calorie: EditText = view.findViewById(R.id.ingredient_calorie)
        private val layout: LinearLayout = view.findViewById(R.id.ingredient_info_linear)

        fun bind(item: RecipeIngredient, position: Int) {
            name.setText(item.name + " ")
            amount.setText(item.amount + "g ")
            try {
                purchase.setText(
                    ((item.cost.toDouble() / item.amountOfPurchase.toDouble()) * item.amount.toDouble()).toInt()
                        .toString() + "원 "
                )
            } catch (e: Exception) {
                purchase.setText("0원")
            }
            try {
                calorie.setText(
                    (item.calorie.toDouble() * item.amount.toDouble() / 100).toInt()
                        .toString() + "kcal"
                )

            } catch (e: Exception) {
                calorie.setText("0g")
            }
            layout.setOnClickListener {
                itemClickListener?.invoke(position)
            }
            name.setOnClickListener {
                itemClickListener?.invoke(position)
            }
            amount.setOnClickListener {
                itemClickListener?.invoke(position)
            }
            purchase.setOnClickListener {
                itemClickListener?.invoke(position)
            }
            calorie.setOnClickListener {
                itemClickListener?.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_recipe_ingredient_info_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredientList[position], position)
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
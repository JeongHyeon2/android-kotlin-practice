package my.app.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import my.app.cooking_app.R
import my.app.cooking_app.models.RecipeIngredient


class MyRecipeIngredientAdapter() : RecyclerView.Adapter<MyRecipeIngredientAdapter.ViewHolder>() {
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
        private val layout: LinearLayout = view.findViewById(R.id.ingredient_item_linear)


        fun bind(item: RecipeIngredient) {
            name.setText(item.name + " ")
            if (item.amount.contains("개")) {
                amount.setText(if (item.amount == "개") "0개" else item.amount)
            } else {
                amount.setText(item.amount + "g")
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
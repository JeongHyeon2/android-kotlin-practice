package my.app.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import my.app.cooking_app.R

class CreateRecipeRVAdapter() : RecyclerView.Adapter<CreateRecipeRVAdapter.ViewHolder>() {
    private var recipe: List<String> =  emptyList()
    private var editTextChangeListener: ((text: String,position: Int) -> Unit)? = null
    private var itemClickListener: ((position:Int) -> Unit)? = null
    fun submitList(newList: List<String>) {
        recipe = newList
        notifyDataSetChanged()
    }
    fun setOnItemClickListener(listener: (position:Int)-> Unit) {
        itemClickListener = listener
    }

    fun setOnEditTextChangeListener(listener: (text: String,position: Int)  -> Unit) {
        editTextChangeListener = listener

    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: EditText = view.findViewById(R.id.my_recipe_create_rv_item_et)
        private val number: TextView = view.findViewById(R.id.my_recipe_create_rv_item_tv_count)

        fun bind(item: String, position: Int) {
            number.text = (position + 1).toString() + "."
            number.setOnClickListener {
                itemClickListener?.invoke(position)
            }
            content.setText(item)
            content.addTextChangedListener {
                editTextChangeListener?.invoke(content.text.toString(),position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRecipeRVAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_recipe_create_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateRecipeRVAdapter.ViewHolder, position: Int) {
        holder.bind(recipe[position], position)
    }

    override fun getItemCount(): Int {
        return recipe.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

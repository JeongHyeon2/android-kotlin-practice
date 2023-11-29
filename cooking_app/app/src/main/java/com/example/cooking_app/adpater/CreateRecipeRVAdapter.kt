package com.example.cooking_app.adpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.R
import com.example.cooking_app.models.ContentModel

class CreateRecipeRVAdapter : RecyclerView.Adapter<CreateRecipeRVAdapter.ViewHolder>() {
    private var recipeList: List<ContentModel> = emptyList()
    private var buttonClickListener: ((position: Int) -> Unit)? = null
    private var editTextChangeListener: ((text: String,position: Int) -> Unit)? = null

    fun submitList(newList: List<ContentModel>) {
        recipeList = newList
        notifyDataSetChanged()
    }


    fun setOnButtonClickListener(listener: (position: Int) -> Unit) {
        buttonClickListener = listener
    }
    fun setOnEditTextChangeListener(listener: (text: String,position: Int)  -> Unit) {
        editTextChangeListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val content: EditText = view.findViewById(R.id.my_recipe_create_rv_item_et)
        private val iv: ImageView = view.findViewById(R.id.my_recipe_create_rv_item_iv)
        private val number: TextView = view.findViewById(R.id.my_recipe_create_rv_item_tv_count)
        private val btn: Button = view.findViewById(R.id.my_recipe_create_rv_item_btn)

        fun bind(item: ContentModel, position: Int) {
            number.text = (position + 1).toString() + "."
            content.setText(item.title)
            if (item.image != null) {
                iv.setImageBitmap(item.image)
            }
            btn.setOnClickListener {
                buttonClickListener?.invoke(adapterPosition)
            }
            content.addTextChangedListener {
                editTextChangeListener?.invoke(content.text.toString(),position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_recipe_create_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position], position)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

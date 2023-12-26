package com.example.cooking_app.adpater

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeModelWithId
import com.example.cooking_app.room.MyDatabase
import com.example.cooking_app.utils.App
import com.example.cooking_app.utils.ImageSave.Companion.loadBitmapFromFilePath
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
                CoroutineScope(Dispatchers.IO).launch {
                    val image = loadBitmapFromFilePath(item.model.image)
                    withContext(Dispatchers.Main) {
                        if (image != null) {
                            iv.setImageBitmap(image)
                            val roundedBitmap =
                                Bitmap.createBitmap(image!!.width, image.height, image.config)
                            val canvas = Canvas(roundedBitmap)
                            val paint = Paint()
                            val rect = Rect(0, 0, image.width, image.height)
                            val rectF = RectF(rect)
                            val radius = 100f
                            paint.isAntiAlias = true
                            canvas.drawRoundRect(rectF, radius, radius, paint)
                            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                            canvas.drawBitmap(image, rect, rect, paint)
                            iv.setImageBitmap(roundedBitmap)
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
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

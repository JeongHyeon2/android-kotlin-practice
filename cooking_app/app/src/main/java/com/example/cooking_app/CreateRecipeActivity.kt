package com.example.cooking_app

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.adpater.CreateRecipeRVAdapter
import com.example.cooking_app.database.MyRecipeDB
import com.example.cooking_app.databinding.ActivityCreateRecipeBinding
import com.example.cooking_app.models.ContentModel
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.viewmodels.CreateRecipeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private val myAdapter = CreateRecipeRVAdapter()
    private val viewModel: CreateRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        var position = 0
        val rv = binding.createRecipeRv
        val getAction =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if(uri!=null) {
                    val oldItem = viewModel.liveRecipeListModel.value!![position]
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    viewModel.updateItem(ContentModel(oldItem.id, oldItem.title, bitmap))
                }
            }

        myAdapter.setOnButtonClickListener {
            Log.d("kkkkk",it.toString())
            position= it
            getAction.launch("image/*")
        }
        myAdapter.setOnEditTextChangeListener {
            text, position ->
           viewModel.updateText(text,position)

        }

        rv.adapter = myAdapter
        rv.layoutManager = LinearLayoutManager(this)

        viewModel.liveRecipeListModel.observe(this, Observer {
            Log.d("asdwqeqweqw","asd")
            myAdapter.submitList(it)
        })


//        // 이미지 선택 버튼에 리스너 등록
//        imageView.setOnClickListener {
//            // 이미지 선택 액티비티 시작
//
//        }

        binding.done.setOnClickListener {
            // 이미지와 텍스트를 사용하여 원하는 작업 수행
            val db = MyRecipeDB.getDatabase(applicationContext)
            CoroutineScope(Dispatchers.IO).launch {
                if(db==null){
                    Log.d("asasdasdsad","dkdkd")

                }
                db.myRecipeDAO().insert(RecipeModel(0,binding.createRecipeEtTitle.text.toString(),null,viewModel.liveRecipeListModel.value!!))
            }
            finish()

        }
        binding.btnAdd.setOnClickListener {
            viewModel.addItem(ContentModel(0,""))
        }
    }
}

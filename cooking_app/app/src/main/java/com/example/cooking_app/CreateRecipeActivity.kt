package com.example.cooking_app


import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateRecipeActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private val myAdapter = CreateRecipeRVAdapter()
    private val viewModel: CreateRecipeViewModel by viewModels()
    private var id: Int = -1
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra("ID_KEY", -1)
        Log.d("ID_KEY",id.toString())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        val rv = binding.createRecipeRv
        if(id>=0) {
            viewModel.getData(id+1)
        }
        val getAction =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if(uri!=null&&position>=0) {
                    Log.d("rv_item_position",position.toString())
                    val oldItem = viewModel.liveRecipeListModel.value!!.contentList[position]
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    viewModel.updateItem(ContentModel(oldItem.id, oldItem.title, bitmap))
                }
            }

        myAdapter.setOnButtonClickListener {
            position = it
            getAction.launch("image/*")
        }
        myAdapter.setOnEditTextChangeListener {
            text, position ->
           viewModel.updateText(text,position)
        }
        binding.createRecipeEtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전에 호출되는 메서드
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 호출되는 메서드
                val newText = s.toString()
                viewModel.editText(newText)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        rv.adapter = myAdapter
        rv.layoutManager = LinearLayoutManager(this)

        viewModel.liveRecipeListModel.observe(this, Observer {
            myAdapter.submitList(it.contentList)
            binding.createRecipeEtTitle.setText(viewModel.liveRecipeListModel.value!!.title)
            Log.d("asasdasdsad",it.contentList.toString())
        })

        binding.done.setOnClickListener {
            // 이미지와 텍스트를 사용하여 원하는 작업 수행
            val db = MyRecipeDB.getDatabase(applicationContext)
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("doneButton",viewModel.liveRecipeListModel.value!!.toString())
                if(id==-1) {
                    db.myRecipeDAO().insert(viewModel.liveRecipeListModel.value!!)
                }else{
                    db.myRecipeDAO().update(viewModel.liveRecipeListModel.value!!)
                }
            }
            finish()

        }
        binding.btnAdd.setOnClickListener {
            viewModel.addItem(ContentModel(0,""))
        }
    }
}

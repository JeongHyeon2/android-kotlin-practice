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
import com.example.cooking_app.viewmodels.CreateRecipeViewModel
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        val rv = binding.createRecipeRv
        if(id>=0) {
            viewModel.getData(id)
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
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = s.toString()
                viewModel.editTitle(newText)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        rv.adapter = myAdapter
        rv.layoutManager = LinearLayoutManager(this)

        viewModel.liveRecipeListModel.observe(this, Observer {
            myAdapter.submitList(it.contentList)
            binding.createRecipeEtTitle.setText(viewModel.liveRecipeListModel.value!!.title)
        })

        binding.done.setOnClickListener {
            viewModel.saveData(id)
            finish()
        }
        binding.btnAdd.setOnClickListener {
            viewModel.addItem(ContentModel(0,""))
        }
    }
}

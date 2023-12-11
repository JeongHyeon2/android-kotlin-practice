package com.example.cooking_app.views


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.utils.App
import com.example.cooking_app.R
import com.example.cooking_app.adpater.CreateRecipeRVAdapter
import com.example.cooking_app.databinding.ActivityCreateRecipeBinding
import com.example.cooking_app.models.ContentModel
import com.example.cooking_app.viewmodels.CreateRecipeViewModel


class CreateRecipeActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private val myAdapter = CreateRecipeRVAdapter()
    private var id: Int = -1
    private var position = -1
    private val viewModel: CreateRecipeViewModel by viewModels { CreateViewModelFactory(intent.getIntExtra("ID_KEY", -1))  }


    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra("ID_KEY", -1)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        val rv = binding.createRecipeRv


        val getAction =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            }

        myAdapter.setOnButtonClickListener {
            position = it
//            getAction.launch("image/*")
            navigatePhotos()

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
            return
        }

        when(requestCode) {
            2000 -> {
                val selectedImageURI : Uri? = data?.data
                if(selectedImageURI!=null&&position>=0) {
                    Log.d("rv_item_position",position.toString())
                    val oldItem = viewModel.liveRecipeListModel.value!!.contentList[position]
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageURI)

                    viewModel.updateItem(ContentModel(oldItem.id, oldItem.title, bitmap))
                }

            } else -> {
            Toast.makeText(this,"잘못된 접근입니다",Toast.LENGTH_SHORT).show()
        }
        }
    }


}
class CreateViewModelFactory(private val id: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateRecipeViewModel::class.java)) {
            return CreateRecipeViewModel(App.context(),id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}




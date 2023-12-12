package com.example.cooking_app.views

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.R
import com.example.cooking_app.adpater.CreateRecipeRVAdapter
import com.example.cooking_app.databinding.ActivityCreateRecipeBinding
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.utils.FBRef
import com.example.cooking_app.viewmodels.CreateRecipeViewModel
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CreateRecipeActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private val myAdapter = CreateRecipeRVAdapter()
    private val viewModel: CreateRecipeViewModel by viewModels()
    private lateinit var key: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         key = intent.getStringExtra("ID_KEY")!!
        if(key == "NONE"){

        }else{
          viewModel.getData(key!!)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        val rv = binding.createRecipeRv

        myAdapter.setOnEditTextChangeListener {
            text, position ->
           viewModel.updateText(text,position)
        }
        viewModel.liveRecipeListModel.observe(this, Observer {
            myAdapter.submitList(it.recipes)
            binding.createRecipeEtTitle.setText(it.title)
        })

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

        binding.done.setOnClickListener {
            save(key!!)
        }
        binding.btnDelete.setOnClickListener {
            viewModel.deleteItem()
        }
        binding.btnAdd.setOnClickListener {
            viewModel.addItem("")
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Log.d("dsadsdsa","ttttttttttttt")
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage("레시피를 저장하지 않았습니다.\n저장 하시겠습니까?")
            .setPositiveButton("저장 후 종료",
                DialogInterface.OnClickListener { dialog, id ->
                    save(key!!)
                })
            .setNegativeButton("저장하지 않고 종료",
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
        builder.show()
    }

    private fun save(key : String){
        if(key!! == "NONE"){
            FBRef.myRecipe.push().setValue(viewModel.liveRecipeListModel.value)
        }else{
            FBRef.myRecipe.child(key).setValue(viewModel.liveRecipeListModel.value)
        }
        Toast.makeText(this,"성공적으로 저장하였습니다",Toast.LENGTH_SHORT).show()
        finish()
    }


}




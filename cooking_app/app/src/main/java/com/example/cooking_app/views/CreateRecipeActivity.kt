package com.example.cooking_app.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val key = intent.getStringExtra("ID_KEY")
        if(key.equals("NONE")){

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
            if(key!! == "NONE"){
                FBRef.myRecipe.push().setValue(viewModel.liveRecipeListModel.value)
            }else{
                FBRef.myRecipe.child(key).setValue(viewModel.liveRecipeListModel.value)
            }
            finish()
        }
        binding.btnAdd.setOnClickListener {
            viewModel.addItem("")
        }
    }
}




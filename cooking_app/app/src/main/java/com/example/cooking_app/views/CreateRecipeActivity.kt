package com.example.cooking_app.views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.R
import com.example.cooking_app.adpater.CreateRecipeRVAdapter
import com.example.cooking_app.adpater.MyRecipeIngredientAdapter
import com.example.cooking_app.adpater.MyRecipeIngredientInfoAdapter
import com.example.cooking_app.databinding.ActivityCreateRecipeBinding
import com.example.cooking_app.fragments.MyDialogFragment
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
    private val myIngredientAdapter = MyRecipeIngredientAdapter()
    private val myIngredientInfoAdapter = MyRecipeIngredientInfoAdapter()
    private val viewModel: CreateRecipeViewModel by viewModels()
    private lateinit var key: String
    private var isChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         var countChanged  = 0
         key = intent.getStringExtra("ID_KEY")!!
        if(key == "NONE"){
        }else{
          viewModel.getData(key!!)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        val rv = binding.createRecipeRv
        val ingredientRv = binding.createRecipeIngredientRv

        viewModel.liveRecipeListModel.observe(this, Observer {
            myIngredientAdapter.submitList(it.ingredients)
            myIngredientInfoAdapter.submitList(it.ingredients)
        })

        myAdapter.setOnEditTextChangeListener {
            text, position ->
           viewModel.updateText(text,position)
            isChanged = true
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
                countChanged++
                if(countChanged>2){
                    isChanged = true
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        rv.adapter = myAdapter
        rv.layoutManager = LinearLayoutManager(this)

        ingredientRv.adapter = myIngredientAdapter
        ingredientRv.layoutManager = GridLayoutManager(this,2)


        binding.done.setOnClickListener {
            save(key!!)
        }
        binding.btnDelete.setOnClickListener {
            viewModel.deleteItem()
            isChanged = true
        }
        binding.btnAdd.setOnClickListener {
            viewModel.addItem("")
            isChanged = true
        }
        binding.createRecipeIngredient.setOnClickListener {
            MyDialogFragment().show(supportFragmentManager,"dialog")
        }
        binding.createRecipeTvIngredient.setOnClickListener {
           it.setBackgroundResource(R.drawable.top_rounded_background_grey)
            binding.createRecipeTvInfo.setBackgroundResource(R.drawable.top_rounded_background_point)
            ingredientRv.adapter = myIngredientAdapter
            ingredientRv.layoutManager = GridLayoutManager(this,2)
        }
        binding.createRecipeTvInfo.setOnClickListener {
            binding.createRecipeTvIngredient.setBackgroundResource(R.drawable.top_rounded_background_point)
           it.setBackgroundResource(R.drawable.top_rounded_background_grey)
            ingredientRv.adapter = myIngredientInfoAdapter
            ingredientRv.layoutManager = LinearLayoutManager(this)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if(isChanged) {
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
        }else{
            finish()
        }
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





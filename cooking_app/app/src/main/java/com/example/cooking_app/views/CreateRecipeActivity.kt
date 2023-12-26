package com.example.cooking_app.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.R
import com.example.cooking_app.adpater.CreateRecipeRVAdapter
import com.example.cooking_app.adpater.MyRecipeIngredientAdapter
import com.example.cooking_app.adpater.MyRecipeIngredientInfoAdapter
import com.example.cooking_app.databinding.ActivityCreateRecipeBinding
import com.example.cooking_app.fragments.MyDialogFragment
import com.example.cooking_app.models.RecipeModelWithId
import com.example.cooking_app.room.MyDatabase
import com.example.cooking_app.utils.App
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.utils.FBRef
import com.example.cooking_app.utils.ImageSave
import com.example.cooking_app.viewmodels.CreateRecipeViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class CreateRecipeActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private val myAdapter = CreateRecipeRVAdapter()
    private val myIngredientAdapter = MyRecipeIngredientAdapter()
    private val myIngredientInfoAdapter = MyRecipeIngredientInfoAdapter()
    private val viewModel: CreateRecipeViewModel by viewModels()
    private lateinit var imageView: ImageView
    private lateinit var viewPhoto: ImageView
    private val resultIntent = Intent()

    private lateinit var uniqueKey: String
    private var isChanged = false
    private var isChangedImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var countChanged = 0
        var countTextChanged = 0
        uniqueKey = intent.getStringExtra("ID_KEY")!!


        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)
        if (uniqueKey == "NONE") {
        } else {
           // viewModel.getData(uniqueKey!!, binding.createRecipeImageview)
            viewModel.getDataFromDB(uniqueKey!!,binding.createRecipeImageview)
        }
        val rv = binding.createRecipeRv
        val ingredientRv = binding.createRecipeIngredientRv
        imageView = binding.createRecipeImageview
        viewPhoto = binding.createRecipeViewPhoto
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                if (uri != null) {
                    isChangedImage = true
                    isChanged = true
                    imageView.setImageURI(uri)
                    viewPhoto.setImageResource(R.drawable.icons_view_image)
                    imageView.visibility = View.VISIBLE

                }
            }
        )
        binding.btnAddIngredient.setOnClickListener {
            MyDialogFragment(-1).show(supportFragmentManager, "dialog")

        }
        viewPhoto.setOnClickListener {
            if (viewPhoto.drawable != null && viewPhoto.drawable.constantState?.equals(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icons_view_image
                    )?.constantState
                ) == true
            ) {
                viewPhoto.setImageResource(R.drawable.icons_not_image)
                imageView.visibility = View.GONE
            } else {
                viewPhoto.setImageResource(R.drawable.icons_view_image)
                imageView.visibility = View.VISIBLE
            }
        }

        viewModel.liveRecipeListModel.observe(this, Observer {
            myIngredientAdapter.submitList(it.ingredients)
            myIngredientInfoAdapter.submitList(it.ingredients)
            binding.createRecipeTvData.text = viewModel.getInformation()
        })
        myAdapter.setOnItemClickListener {
            if (viewModel.liveRecipeListModel.value!!.recipes[it] != "") {
                AlertDialog.Builder(this)
                    .setMessage(
                        (it + 1).toString() + "번. "
                                + viewModel.liveRecipeListModel.value!!.recipes[it]
                                + "\n 삭제하시겠습니까?"
                    )
                    .setPositiveButton("삭제",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.deleteItem(it)
                            isChanged = true
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                    .show()
            } else {
                viewModel.deleteItem(it)
                isChanged = true
            }
        }

        myIngredientInfoAdapter.setOnItemClickListener {
            MyDialogFragment(it).show(supportFragmentManager, "dialog")
        }
        myIngredientAdapter.setOnItemClickListener {
            MyDialogFragment(it).show(supportFragmentManager, "dialog")
        }

        myAdapter.setOnEditTextChangeListener { text, position ->
            viewModel.updateText(text, position)
            countTextChanged++
            if(countTextChanged>=2){ isChanged = true}

        }
        viewModel.liveRecipeListModel.observe(this, Observer {
            myAdapter.submitList(it.recipes)
            binding.createRecipeEtTitle.setText(it.title)
        })
        viewModel.loadingState.observe(this, Observer {

        })



        binding.createRecipeEtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = s.toString()
                viewModel.editTitle(newText)
                countChanged++
                if (countChanged > 2) {
                    isChanged = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        rv.adapter = myAdapter
        rv.layoutManager = LinearLayoutManager(this)

        ingredientRv.adapter = myIngredientAdapter
        ingredientRv.layoutManager = GridLayoutManager(this, 2)


        binding.done.setOnClickListener {
            save(uniqueKey!!)
        }

        binding.btnAdd.setOnClickListener {
            viewModel.addItem("")
            isChanged = true
        }

        binding.createRecipeTvIngredient.setOnClickListener {
            it.setBackgroundResource(com.example.cooking_app.R.drawable.top_rounded_background_grey)
            binding.createRecipeTvInfo.setBackgroundResource(com.example.cooking_app.R.drawable.top_rounded_background_point)
            ingredientRv.adapter = myIngredientAdapter
            ingredientRv.layoutManager = GridLayoutManager(this, 2)
        }
        binding.createRecipeTvInfo.setOnClickListener {
            binding.createRecipeTvIngredient.setBackgroundResource(com.example.cooking_app.R.drawable.top_rounded_background_point)
            it.setBackgroundResource(com.example.cooking_app.R.drawable.top_rounded_background_grey)
            ingredientRv.adapter = myIngredientInfoAdapter
            ingredientRv.layoutManager = LinearLayoutManager(this)
        }
        binding.createRecipeAddPhoto.setOnClickListener {
            getAction.launch("image/*")
        }
        imageView.setOnClickListener {
            getAction.launch("image/*")
        }
        imageView.setOnLongClickListener {
            AlertDialog.Builder(this)
                .setMessage("사진을 삭제하시겠습니까?")
                .setPositiveButton("삭제",
                    DialogInterface.OnClickListener { dialog, id ->
                        isChanged = true
                        imageView.setImageResource(0)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
                .show()
            true
        }

    }


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (isChanged) {
            AlertDialog.Builder(this)
                .setMessage("레시피를 저장하지 않았습니다.\n저장 하시겠습니까?")
                .setPositiveButton("저장 후 종료",
                    DialogInterface.OnClickListener { dialog, id ->
                        save(uniqueKey!!)
                    })
                .setNegativeButton("저장하지 않고 종료",
                    DialogInterface.OnClickListener { dialog, id ->
                        finish()
                    })
                .show()
        } else {
            finish()
        }
    }

    private fun save(key: String) {
        if (isChanged) {
            if (isChangedImage) {
                uploadImage(FBAuth.getUid())
            } else {
                var toggle = false
                if (key == "NONE") {
                    uniqueKey = FBRef.myRecipe.push().key.toString()
                    toggle = true
                }
                FBRef.myRecipe.child(uniqueKey).setValue(viewModel.liveRecipeListModel.value)
                val resultIntent = Intent()
                resultIntent.putExtra("RESULT", uniqueKey)
                setResult(Activity.RESULT_OK, resultIntent)
                val db = MyDatabase.getDatabase(this)

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.setLoadingStateTrue()
                    if(toggle){
                        db.myDao().insert(RecipeModelWithId( uniqueKey,viewModel.liveRecipeListModel.value!!))

                    }else{
                        db.myDao().update(RecipeModelWithId( uniqueKey,viewModel.liveRecipeListModel.value!!))
                    }
                }
                Toast.makeText(this, "성공적으로 저장하였습니다", Toast.LENGTH_SHORT).show()

                resultIntent.putExtra("RESULT", uniqueKey)
                setResult(Activity.RESULT_OK, resultIntent)
                viewModel.setLoadingStateFalse()
                finish()
            }
        }else{
            finish()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage(uid: String) {
        val db = MyDatabase.getDatabase(App.context())

        var toggle = false
        val storage = Firebase.storage
        var storageRef :StorageReference? = null
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.setLoadingStateTrue()
        if (uniqueKey == "NONE") {
                uniqueKey = FBRef.myRecipe.push().key.toString()
                toggle = true
            }
            storageRef = storage.reference.child("$uid.$uniqueKey.png")

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val data = baos.toByteArray()
            var isSuccess = false


        var uploadTask = storageRef!!.putBytes(data)
            GlobalScope.launch(Dispatchers.IO){
                val targetTimeMillis = System.currentTimeMillis() + 10000 // 현재 시간에 10초(10000밀리초)를 더한 값

                while (System.currentTimeMillis() < targetTimeMillis) {

                }
                uploadTask.cancel()
                withContext(Dispatchers.Main){
                  if(!isSuccess) {
                      Toast.makeText(baseContext, "네트워크 연결을 확인해주세요", Toast.LENGTH_SHORT).show()
                  }
                    viewModel.setLoadingStateFalse()
                    return@withContext
                }

            }
            uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

            GlobalScope.launch(Dispatchers.Main) {
                viewModel.editImage(taskSnapshot.storage.name)

                FBRef.myRecipe.child(uniqueKey).setValue(viewModel.liveRecipeListModel.value)
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(toggle){
                                db.myDao().insert(RecipeModelWithId( uniqueKey,viewModel.liveRecipeListModel.value!!))
                            }else{
                                db.myDao().update(RecipeModelWithId( uniqueKey,viewModel.liveRecipeListModel.value!!))
                            }

                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            ImageSave.saveBitmapToInternalStorage(applicationContext,bitmap,viewModel.liveRecipeListModel.value!!.image)
                        }

                        resultIntent.putExtra("RESULT", uniqueKey)
                        setResult(Activity.RESULT_OK, resultIntent)

                        Toast.makeText(
                            this@CreateRecipeActivity,
                            "성공적으로 저장하였습니다!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        isSuccess = true
                        viewModel.setLoadingStateFalse()
                        finish()
                    }
            }
        }
        }
    }

}





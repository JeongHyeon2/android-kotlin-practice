package com.example.cooking_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.adpater.MyRecipeRVAdapter
import com.example.cooking_app.database.MyRecipeDB
import com.example.cooking_app.databinding.ActivityCreateRecipeBinding
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRecipeBinding
    private lateinit var imageView: ImageView

    // 이미지 선택을 위한 ActivityResultLauncher 선언
    private val getAction =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // 이미지 뷰에 선택한 이미지 표시
            imageView.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_recipe)

        imageView = binding.createRecipeIv

        // 이미지 선택 버튼에 리스너 등록
        imageView.setOnClickListener {
            // 이미지 선택 액티비티 시작
            getAction.launch("image/*")
        }

        binding.done.setOnClickListener {
            // 이미지와 텍스트를 사용하여 원하는 작업 수행
            val db = MyRecipeDB.getDatabase(applicationContext)
            CoroutineScope(Dispatchers.IO).launch {
                if(db==null){
                    Log.d("asasdasdsad","dkdkd")

                }
                db.myRecipeDAO().insert(RecipeModel(0, binding.createRecipeEt.text.toString(),imageView.drawToBitmap()))
            }
            finish()
        }
    }
}

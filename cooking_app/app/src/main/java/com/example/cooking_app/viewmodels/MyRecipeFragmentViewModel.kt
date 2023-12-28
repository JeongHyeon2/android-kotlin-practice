package com.example.cooking_app.viewmodels

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.models.RecipeModelWithId
import com.example.cooking_app.room.MyDatabase
import com.example.cooking_app.utils.App
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.utils.FBRef
import com.example.cooking_app.utils.ImageSave
import com.example.cooking_app.utils.ImageSave.Companion.imageSaveLink
import com.example.cooking_app.utils.ImageSave.Companion.loadBitmapFromFilePath
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MyRecipeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private var _mutableRecipeListModel =
        MutableLiveData<MutableList<RecipeModelWithId>>(mutableListOf())
    val liveRecipeListModel: LiveData<MutableList<RecipeModelWithId>> get() = _mutableRecipeListModel

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val db = MyDatabase.getDatabase(application.baseContext)

    private fun deleteItem(key: String) = viewModelScope.launch(Dispatchers.IO) {
        FBRef.myRecipe.child(key).removeValue()
        val storageReference =
            FirebaseStorage.getInstance().reference.child(FBAuth.getUid() + "." + key + ".jpg")
        storageReference.delete()
    }


    fun isEmpty() = _mutableRecipeListModel.value!!.isEmpty()
    private fun getData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                viewModelScope.launch(Dispatchers.IO) {
                    val new = mutableListOf<RecipeModelWithId>()
                    for (dataModel in dataSnapshot.children) {
                        val data = dataModel.getValue(RecipeModel::class.java)
                        new.add(RecipeModelWithId(dataModel.key.toString(), data!!))
                    }
                    new.reverse()
                    withContext(Dispatchers.Main) {
                        _mutableRecipeListModel.value = new
                    }

                    for (data in _mutableRecipeListModel.value!!) {
                        db.myDao().insert(data)
                        if (data.model.image != "") {
                            val storageRef =
                                Firebase.storage.reference.child(data.model.image)
                            storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    GlobalScope.launch(Dispatchers.IO) {
                                            Glide.with(App.context()).asBitmap().load(task.result)
                                                .into(object : CustomTarget<Bitmap>() {
                                                    override fun onResourceReady(
                                                        resource: Bitmap,
                                                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                                                    ) {
                                                        ImageSave.saveBitmapToInternalStorage(App.context(),resource,data.model.image)

                                                    }

                                                    override fun onLoadCleared(placeholder: Drawable?) {
                                                        TODO("Not yet implemented")
                                                    }
                                                })
                                    }
                                }
                            })
                        }
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(App.context(), "서버로부터 데이터를 가져왔습니다.", Toast.LENGTH_SHORT)
                            .show()
                        _loadingState.value = false
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled if needed
            }
        }

        FBRef.myRecipe.addListenerForSingleValueEvent(postListener)


    }

    fun getDataFromFB() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            _loadingState.value = true
        }
        ImageSave.deletePngFilesInDirectory()
        db.myDao().deleteAll()
        getData()
    }


    fun getDataFromDB() = viewModelScope.launch(Dispatchers.IO) {
        val dataFromDB = db.myDao().getAllDataWithFlow()

        dataFromDB.collect { recipeList ->
            withContext(Dispatchers.Main) {
                _mutableRecipeListModel.value = recipeList.toMutableList()
            }
        }
    }

    fun saveDataToFB() = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            _loadingState.value = true
        }
        val recipeData = db.myDao().getAllData()
        for (recipe in recipeData) {
            FBRef.myRecipe.child(recipe.id).setValue(recipe.model)
            val image =
                loadBitmapFromFilePath(recipe.model.image)
            var storageRef = Firebase.storage.reference.child(recipe.model.image)
            val baos = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.JPEG, 10, baos)
            val data = baos.toByteArray()
            storageRef.putBytes(data)
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(App.context(), "서버에 데이터를 저장했습니다.", Toast.LENGTH_SHORT).show()
            _loadingState.value = false
        }
    }



    fun getOneData(id: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = db.myDao().getOneData(id)
        withContext(Dispatchers.Main) {
            val old = _mutableRecipeListModel.value
            val index = old!!.indexOfFirst { it.id == id }
            if (index != -1) {
                old[index] = data
            }
            _mutableRecipeListModel.value = old!!
        }
    }

    fun deleteDialog(position: Int, item: RecipeModelWithId, thisContext: Context) {
        val builder = AlertDialog.Builder(thisContext)
        builder.setTitle("삭제 확인")
            .setMessage("정말 " + item.model.title + "을/를 삭제하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->

                    viewModelScope.launch(Dispatchers.IO) {
                        db.myDao().delete(item)
                    }
                    deleteItem(item.id)
                    val list = _mutableRecipeListModel.value!!
                    list.removeAt(position)
                    _mutableRecipeListModel.value = list

                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.show()
    }
}
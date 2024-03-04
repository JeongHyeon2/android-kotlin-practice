package my.app.cooking_app.viewmodels

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import my.app.cooking_app.models.RecipeIngredient
import my.app.cooking_app.models.RecipeModel
import my.app.cooking_app.room.MyDatabase
import my.app.cooking_app.utils.App
import my.app.cooking_app.utils.FBRef
import my.app.cooking_app.utils.ImageSave.Companion.loadBitmapFromFilePath
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateRecipeViewModel() : ViewModel() {
    private var _mutableRecipeListModel = MutableLiveData<RecipeModel>(
        RecipeModel(
            "",
            mutableListOf(""),
            mutableListOf(),
            ""
        )
    )

    val liveRecipeListModel: LiveData<RecipeModel> get() = _mutableRecipeListModel

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val db = MyDatabase.getDatabase(App.context())

    fun setLoadingStateTrue() = viewModelScope.launch(Dispatchers.Main) {
        _loadingState.value = true
    }

    fun setLoadingStateFalse() = viewModelScope.launch(Dispatchers.Main) {
        _loadingState.value = false
    }


    fun addItem(item: String) {
        try {
            val old = _mutableRecipeListModel.value!!
            val newList = _mutableRecipeListModel.value!!.recipes
            newList.add(item)
            val new = RecipeModel(old.title, newList, old.ingredients, old.image)
            _mutableRecipeListModel.value = new
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
    }

    fun editImage(src: String) {
        try {
            val old = _mutableRecipeListModel.value!!
            val new = RecipeModel(old.title, old.recipes, old.ingredients, src)
            _mutableRecipeListModel.value = new
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
    }

    fun addIngredient(ingredient: RecipeIngredient) {
        try {
            val old = _mutableRecipeListModel.value!!
            val newList = _mutableRecipeListModel.value!!.ingredients
            newList.add(ingredient)
            val new = RecipeModel(old.title, old.recipes, newList, old.image)
            _mutableRecipeListModel.value = new
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
    }

    fun editIngredient(ingredient: RecipeIngredient, position: Int) {
        try {
            val old = _mutableRecipeListModel.value!!
            val newList = _mutableRecipeListModel.value!!.ingredients
            newList[position] = ingredient
            val new = RecipeModel(old.title, old.recipes, newList, old.image)
            _mutableRecipeListModel.value = new
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }

    }

    fun editTitle(text: String) {
        _mutableRecipeListModel.value!!.title = text
    }

    fun updateText(text: String, position: Int) {
        val currentList = _mutableRecipeListModel.value?.recipes!!
        currentList[position] = text
    }

    fun getData(key: String, iv: ImageView) = viewModelScope.launch(Dispatchers.IO) {
        FBRef.myRecipe.child(key).get().addOnSuccessListener {
            _mutableRecipeListModel.value = it.getValue(RecipeModel::class.java)
            val link = liveRecipeListModel.value!!.image
            if (!(link == "" || link == null)) {
                val storageRef = Firebase.storage.reference.child(liveRecipeListModel.value!!.image)
                storageRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(App.context()).load(task.result)
                            .into(iv)
                        iv.visibility = View.GONE
                    }
                })
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun getDataFromDB(key: String, iv: ImageView) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val data = db.myDao().getOneData(key).model
            val image = loadBitmapFromFilePath(data.image)
            withContext(Dispatchers.Main) {
                _mutableRecipeListModel.value = data
                if (image != null) {
                    iv.setImageBitmap(image)
                }

                iv.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
    }

    fun deleteItem(position: Int) {
        try {
            if (_mutableRecipeListModel.value!!.recipes.size == 1) return
            val old = _mutableRecipeListModel.value!!
            val newList = _mutableRecipeListModel.value!!.recipes
            newList.removeAt(position)
            val new = RecipeModel(old.title, newList, old.ingredients, old.image)
            _mutableRecipeListModel.value = new
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
    }

    fun deleteRecipe(index: Int) {
        try {
            val old = _mutableRecipeListModel.value!!
            val newList = _mutableRecipeListModel.value!!.ingredients
            newList.removeAt(index)
            val new = RecipeModel(old.title, old.recipes, newList, old.image)
            _mutableRecipeListModel.value = new
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
    }

    fun getInformation(): String {
        var cost = 0
        var cal = 0
        try {
            _mutableRecipeListModel.value!!.ingredients.map {
                try {
                    cost += ((it.cost.toDouble() / it.amountOfPurchase.toDouble()) * it.amount.toDouble()).toInt()
                    cal += (it.calorie.toDouble() * it.amount.toDouble() / 100).toInt()

                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
            Log.d("CreateRecipeViewModel", e.toString())
        }
        return "가격: " + cost.toString() + "원  열량: " + cal.toString() + "kcal"

    }
}
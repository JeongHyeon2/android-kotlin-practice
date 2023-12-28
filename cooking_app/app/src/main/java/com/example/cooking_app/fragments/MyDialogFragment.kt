package com.example.cooking_app.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.R
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.models.RecipeIngredientForDB
import com.example.cooking_app.utils.FBRef
import com.example.cooking_app.viewmodels.CreateRecipeViewModel
import com.example.cooking_app.viewmodels.IngredientFragmentViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyDialogFragment(
    private val position: Int,
    private var isIngredientFragment: Boolean = false
) : DialogFragment() {
    private lateinit var viewModel: CreateRecipeViewModel
    private lateinit var viewModelIngredient: IngredientFragmentViewModel
    private lateinit var name :EditText
    private lateinit var ingredientAmount :EditText
    private lateinit var purchase :EditText
    private lateinit var amount :EditText
    private lateinit var cal:EditText
    private lateinit var loadingProgressBar :ProgressBar
    private lateinit var  getIngredientBtn :Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CreateRecipeViewModel::class.java]
        viewModelIngredient =
            ViewModelProvider(requireActivity())[IngredientFragmentViewModel::class.java]
         name = view.findViewById<EditText>(R.id.dialog_ingredient_name)
         ingredientAmount = view.findViewById<EditText>(R.id.dialog_ingredient_amount)
         purchase = view.findViewById<EditText>(R.id.dialog_ingredient_purchase)
         amount = view.findViewById<EditText>(R.id.dialog_ingredient_purchase_amount)
         cal = view.findViewById<EditText>(R.id.dialog_ingredient_calorie)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar_dialog)
        getIngredientBtn =  view.findViewById<Button>(R.id.dialog_button_get_ingredient)
        if (isIngredientFragment) {
            ingredientAmount.visibility = View.GONE
        }
        if (position >= 0) {
            if (isIngredientFragment) {
                val item = viewModelIngredient.ingredients.value!![position]
                ingredientAmount.setText("0")
                name.setText(item.name)
                purchase.setText(item.cost)
                amount.setText(item.amountOfPurchase)
                cal.setText(item.calorie)
            } else {
                val item = viewModel.liveRecipeListModel.value!!.ingredients[position]
                name.setText(item.name)
                ingredientAmount.setText(item.amount)
                purchase.setText(item.cost)
                amount.setText(item.amountOfPurchase)
                cal.setText(item.calorie)
            }

        }
        view.findViewById<Button>(R.id.dialog_button_save).setOnClickListener {
            if (view.findViewById<EditText>(R.id.dialog_ingredient_name).text.toString() == "") {
                Toast.makeText(view.context, "재료명을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val ingredient = RecipeIngredient(
                    name.text.toString(),
                    ingredientAmount.text.toString().ifEmpty { "0" },
                    purchase.text.toString(),
                    amount.text.toString(),
                    cal.text.toString(),
                )

                if (position >= 0) {
                    if (isIngredientFragment) {
                        viewModelIngredient.edit(position, ingredient)
                    } else {
                        viewModel.editIngredient(ingredient, position)
                    }
                } else {
                    if (isIngredientFragment) {
                        viewModelIngredient.add(ingredient)
                    } else {
                        viewModel.addIngredient(
                            ingredient
                        )
                    }
                }
                Toast.makeText(view.context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        val deleteBtn = view.findViewById<Button>(R.id.dialog_button_delete)
        if (position < 0) {
            deleteBtn.visibility = View.GONE
            if (!isIngredientFragment) {
                getIngredientBtn.visibility = View.VISIBLE
            }
        }
        getIngredientBtn.setOnClickListener {
            showListPopup()
        }
        deleteBtn.setOnClickListener {
            if (isIngredientFragment) {
                viewModelIngredient.delete(position)
            } else {
                viewModel.deleteRecipe(position)
            }
            Toast.makeText(view.context, "삭제되었습니다", Toast.LENGTH_SHORT).show()

            dismiss()
        }

    }

    private fun showListPopup() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                loadingProgressBar.visibility = View.VISIBLE
                getIngredientBtn.visibility = View.GONE
            }
            val list = mutableListOf<RecipeIngredientForDB>()
            val stringList = mutableListOf<String>()

            FBRef.myIngredients.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val value = postSnapshot.getValue(RecipeIngredientForDB::class.java)
                        list.add(value!!)
                        stringList.add(value.name)
                    }
                    list.reverse()
                    stringList.reverse()

                    // UI 업데이트를 Main 스레드에서 처리
                    view?.post {

                        val adapter =
                            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, stringList)

                        val listView = ListView(requireContext())
                        listView.adapter = adapter

                        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
                        builder.setTitle("재료를 선택하세요")
                            .setView(listView)
                            .setNegativeButton("취소") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = builder.create()
                        dialog.show()
                        loadingProgressBar.visibility = View.GONE
                        getIngredientBtn.visibility = View.VISIBLE

                        listView.setOnItemClickListener { _, _, position, _ ->
                            val selectedItem = list[position]
                            name.setText(selectedItem.name)
                            amount.setText(selectedItem.amountOfPurchase)
                            purchase.setText(selectedItem.cost)
                            cal.setText(selectedItem.calorie)
                            dialog.dismiss()
                            ingredientAmount.requestFocus()
                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 데이터 읽기가 취소될 때 처리
                    println("Read failed: " + databaseError.toException())
                }
            })
        }
    }

}
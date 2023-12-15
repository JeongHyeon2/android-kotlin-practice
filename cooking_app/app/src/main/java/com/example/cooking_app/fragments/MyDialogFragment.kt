package com.example.cooking_app.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.R
import com.example.cooking_app.databinding.FragmentMyRecipeBinding
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.viewmodels.CreateRecipeViewModel

class MyDialogFragment(private val position: Int) : DialogFragment() {
    private lateinit var viewModel: CreateRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CreateRecipeViewModel::class.java]
        val name = view.findViewById<EditText>(R.id.dialog_ingredient_name)
        val ingredientAmount = view.findViewById<EditText>(R.id.dialog_ingredient_amount)
        val purchase = view.findViewById<EditText>(R.id.dialog_ingredient_purchase)
        val amount = view.findViewById<EditText>(R.id.dialog_ingredient_purchase_amount)
        val cal = view.findViewById<EditText>(R.id.dialog_ingredient_calorie)
        if (position >= 0) {
            val item = viewModel.liveRecipeListModel.value!!.ingredients[position]
            name.setText(item.name)
            ingredientAmount.setText(item.amount)
            purchase.setText(item.cost)
            amount.setText(item.amountOfPurchase)
            cal.setText(item.calorie)

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
                    viewModel.editIngredient(ingredient, position)
                } else {
                    viewModel.addIngredient(
                        ingredient

                    )
                }
                Toast.makeText(view.context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        val deleteBtn = view.findViewById<Button>(R.id.dialog_button_delete)
        if (position < 0) {
            deleteBtn.visibility = View.INVISIBLE
        }
        deleteBtn.setOnClickListener {
            viewModel.deleteRecipe(position)
            Toast.makeText(view.context, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }
}
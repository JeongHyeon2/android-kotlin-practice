package com.example.cooking_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.R
import com.example.cooking_app.databinding.FragmentMyRecipeBinding
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.viewmodels.CreateRecipeViewModel

class MyDialogFragment : DialogFragment() {
        private lateinit var viewModel: CreateRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CreateRecipeViewModel::class.java]
        view.findViewById<Button>(R.id.dialog_button_save).setOnClickListener {
            viewModel.addIngredient(
                RecipeIngredient(
                    view.findViewById<EditText>(R.id.dialog_ingredient_name).text.toString(),
                    view.findViewById<EditText>(R.id.dialog_ingredient_amount).text.toString(),
                    view.findViewById<EditText>(R.id.dialog_ingredient_purchase).text.toString(),
                    view.findViewById<EditText>(R.id.dialog_ingredient_purchase_amount).text.toString(),
                    view.findViewById<EditText>(R.id.dialog_ingredient_calorie).text.toString(),
                    )
            )

        }

    }
}
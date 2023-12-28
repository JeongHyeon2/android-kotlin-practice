package com.example.cooking_app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.R
import com.example.cooking_app.adpater.IngredientFragmentAdapter
import com.example.cooking_app.databinding.FragmentIngredientBinding
import com.example.cooking_app.models.RecipeIngredient
import com.example.cooking_app.viewmodels.IngredientFragmentViewModel


class IngredientFragment : Fragment() {
    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private val myAdapter = IngredientFragmentAdapter()
    private lateinit var viewModel: IngredientFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(requireActivity())[IngredientFragmentViewModel::class.java]
        viewModel.ingredients.observe(viewLifecycleOwner, Observer {
            myAdapter.submitList(it)
        })
        binding.ingredientFragmentAdd.setOnClickListener {
            MyDialogFragment(-1, true).show(childFragmentManager, "dialog")
        }
        binding.ingredientFragmentRv.adapter = myAdapter
        binding.ingredientFragmentRv.layoutManager = LinearLayoutManager(requireContext())
        myAdapter.setOnItemClickListener {
            MyDialogFragment(it, true).show(
                childFragmentManager,
                "dialog"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }



}
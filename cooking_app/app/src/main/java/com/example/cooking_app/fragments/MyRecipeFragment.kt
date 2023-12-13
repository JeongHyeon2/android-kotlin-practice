package com.example.cooking_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cooking_app.views.CreateRecipeActivity
import com.example.cooking_app.adpater.MyRecipeRVAdapter
import com.example.cooking_app.databinding.FragmentMyRecipeBinding
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.utils.FBRef
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyRecipeFragment() : Fragment() {
    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private val myAdapter = MyRecipeRVAdapter()
    private val viewModel: MyRecipeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyRecipeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        val rv = binding.myRecipeRv
        rv.adapter = myAdapter
        rv.layoutManager = GridLayoutManager(activity, 2)
        binding.viewModel = viewModel
        viewModel.getData()
        viewModel.liveRecipeListModel.observe(viewLifecycleOwner, Observer {
            myAdapter.submitList(it)
        })
        myAdapter.setOnItemClickListener { position ->
            val intent = Intent(activity, CreateRecipeActivity::class.java)
            intent.putExtra("ID_KEY", viewModel.liveRecipeListModel.value!![position].id)
            startActivity(intent)
        }
        myAdapter.setOnLongItemClickListener { position ->
            viewModel.deleteDialog(
                viewModel.liveRecipeListModel.value!![position],
                requireContext()
            )
        }
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            Log.d("qwerqwer", it.toString())
        })
        binding.myRecipeFab.setOnClickListener {
            val intent = Intent(activity, CreateRecipeActivity::class.java)
            intent.putExtra("ID_KEY", "NONE")
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

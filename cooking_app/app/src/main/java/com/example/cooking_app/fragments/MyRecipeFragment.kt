package com.example.cooking_app.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cooking_app.CreateRecipeActivity
import com.example.cooking_app.R
import com.example.cooking_app.adpater.MyRecipeRVAdapter
import com.example.cooking_app.database.ContentModelConverters
import com.example.cooking_app.databinding.FragmentMyRecipeBinding
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope

class MyRecipeFragment() : Fragment() {
    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private  val viewModel: MyRecipeFragmentViewModel by activityViewModels()
    private val myAdapter = MyRecipeRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        viewModel.liveRecipeListModel.observe(viewLifecycleOwner, Observer {
            myAdapter.submitList(it)
        })

        myAdapter.setOnItemClickListener { position ->
            val intent = Intent(activity, CreateRecipeActivity::class.java)
            intent.putExtra("ID_KEY", viewModel.liveRecipeListModel.value!![position].id)
            startActivity(intent)
        }
        myAdapter.setOnLongItemClickListener { position->
            viewModel.deleteDialog(position,requireContext())
            Log.d("sdsasadadsdsasd",viewModel.liveRecipeListModel.value!!.toString())
        }
        binding.myRecipeFab.setOnClickListener {
            val intent = Intent(activity, CreateRecipeActivity::class.java)
            intent.putExtra("ID_KEY", -1)
            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

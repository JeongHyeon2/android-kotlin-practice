package com.example.cooking_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.cooking_app.databinding.FragmentMyRecipeBinding
import com.example.cooking_app.models.RecipeModel
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import kotlinx.coroutines.CoroutineScope

class MyRecipeFragment : Fragment() {
    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private  val viewModel: MyRecipeFragmentViewModel by viewModels()
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
            Log.d("dddd","dd")
        })


        binding.myRecipeFab.setOnClickListener {
            startActivity(Intent(activity, CreateRecipeActivity::class.java))
            // 이 부분 solo life app에 북마크 기능 있는데 그거 한 번 보기


        }
    }

    override fun onStart() {
        super.onStart()
       Log.d( "hihi",viewModel.liveRecipeListModel.value.toString())
        viewModel.getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

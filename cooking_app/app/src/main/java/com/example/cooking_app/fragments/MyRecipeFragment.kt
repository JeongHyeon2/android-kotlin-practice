package com.example.cooking_app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cooking_app.adpater.MyRecipeRVAdapter
import com.example.cooking_app.databinding.FragmentMyRecipeBinding
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import com.example.cooking_app.views.CreateRecipeActivity

class MyRecipeFragment() : Fragment() {
    private var _binding: FragmentMyRecipeBinding? = null
    private val binding get() = _binding!!
    private val myAdapter = MyRecipeRVAdapter()
    private lateinit var viewModel: MyRecipeFragmentViewModel
    private var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyRecipeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MyRecipeFragmentViewModel::class.java]
        binding.lifecycleOwner = this
        val rv = binding.myRecipeRv
        rv.adapter = myAdapter
        rv.layoutManager = GridLayoutManager(activity, 2)
        binding.viewModel = viewModel
        if (isFirst) {
            viewModel.getDataFromDB()
            isFirst = false
        }
        binding.myRecipeRv.apply { itemAnimator = null }

        viewModel.liveRecipeListModel.observe(viewLifecycleOwner, Observer {
            myAdapter.submitList(it)
            if (viewModel.isEmpty()) {
                binding.myRecipeTvBase.visibility = View.VISIBLE
            } else {
                binding.myRecipeTvBase.visibility = View.GONE
            }
        })


        myAdapter.setOnItemClickListener { position ->
            val intent = Intent(activity, CreateRecipeActivity::class.java)
            intent.putExtra("ID_KEY", viewModel.liveRecipeListModel.value!![position].id)
            startActivityForResult(intent, 100)
        }
        myAdapter.setOnLongItemClickListener { position ->
            viewModel.deleteDialog(
                position,
                viewModel.liveRecipeListModel.value!![position],
                requireContext()
            )
        }

        binding.myRecipeFab.setOnClickListener {
            val intent = Intent(activity, CreateRecipeActivity::class.java)
            intent.putExtra("ID_KEY", "NONE")
            startActivityForResult(intent, 100)
        }
        binding.myRecipeSearchBtn.setOnClickListener {
            val position =
                viewModel.findDataByName(binding.myRecipeSearch.text.toString().replace(" ", ""))
            if (position >= 0) {
                binding.myRecipeRv.scrollToPosition(position)
            } else {
                Toast.makeText(requireContext(), "존재하지 않는 요리명입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            // 처리할 결과가 있을 경우 여기에서 처리
            if (data != null) {
                val result = data.getStringExtra("RESULT")
                viewModel.getOneData(result.toString())
            }
        }

    }
}

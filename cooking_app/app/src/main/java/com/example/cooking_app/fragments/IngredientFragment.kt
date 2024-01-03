package com.example.cooking_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cooking_app.adpater.IngredientFragmentAdapter
import com.example.cooking_app.databinding.FragmentIngredientBinding
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
            if (it.size == 0 && !viewModel.loadingState.value!!) {
                binding.ingredientTextview.visibility = View.VISIBLE
            } else {
                binding.ingredientTextview.visibility = View.GONE
            }
        })
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.ingredientTextview.visibility = View.GONE
            }else{
                if (viewModel.ingredients.value!!.size == 0 && !it) {
                    binding.ingredientTextview.visibility = View.VISIBLE
                }
            }
        })
        binding.vm = viewModel
        binding.lifecycleOwner = this

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
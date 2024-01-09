package com.example.cooking_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    ): View {
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
            if(viewModel.ingredients.value!!.size>=1000){
                Toast.makeText(requireContext(),"최대 재료개수 1000개를 초과하였습니다!",Toast.LENGTH_SHORT).show()
        }else{
                MyDialogFragment(-1, true).show(childFragmentManager, "dialog")

            }
        }
        binding.ingredientFragmentRv.adapter = myAdapter
        binding.ingredientFragmentRv.layoutManager = LinearLayoutManager(requireContext())
        myAdapter.setOnItemClickListener {
            MyDialogFragment(it, true).show(
                childFragmentManager,
                "dialog"
            )
        }
        binding.myIngredientSearchBtn.setOnClickListener {
            val position =  viewModel.findDataByName(binding.myIngredientSearch.text.toString())
            if(position>=0){
                binding.ingredientFragmentRv.scrollToPosition(position)
            }else{
                Toast.makeText(requireContext(), "존재하지 않는 재료명입니다.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
    }


}
package com.example.coin_app.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coin_app.R
import com.example.coin_app.databinding.FragmentCoinListBinding
import com.example.coin_app.db.entity.InterestCoinEntity
import com.example.coin_app.view.adapter.CoinListRVAdapter
import timber.log.Timber


class CoinListFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding : FragmentCoinListBinding? = null
    private val binding get()=_binding!!
    private val selectedList = ArrayList<InterestCoinEntity>()
    private val unselectedList = ArrayList<InterestCoinEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllInterestCoinData()
        viewModel.selectedCoinList.observe(viewLifecycleOwner, Observer {
            selectedList.clear()
            unselectedList.clear()
           for (item in it){
               if(item.selected){
                   selectedList.add(item)
               }else{
                   unselectedList.add(item)
               }
           }
            setSelectedListRV()
        })
    }
    private fun setSelectedListRV(){
        val rvadaper = CoinListRVAdapter(requireContext(),selectedList)
        binding.selectedCoinRV.adapter = rvadaper
        binding.selectedCoinRV.layoutManager = LinearLayoutManager(requireContext())

        rvadaper.itemClick = object :CoinListRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
               viewModel.updateInterestCoinData(selectedList[position])
            }

        }
        val rvadaper2 = CoinListRVAdapter(requireContext(),unselectedList)
        binding.unSelectedCoinRV.adapter = rvadaper2
        binding.unSelectedCoinRV.layoutManager = LinearLayoutManager(requireContext())
        rvadaper2.itemClick = object :CoinListRVAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                viewModel.updateInterestCoinData(unselectedList[position])
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
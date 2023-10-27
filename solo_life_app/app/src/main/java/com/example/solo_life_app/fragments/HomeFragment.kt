package com.example.solo_life_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solo_life_app.R
import com.example.solo_life_app.contents_list.BookmarkRVAdapter
import com.example.solo_life_app.contents_list.ContentModel
import com.example.solo_life_app.databinding.FragmentHomeBinding
import com.example.solo_life_app.util.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var myAdapter: BookmarkRVAdapter
    val items = ArrayList<ContentModel>()
    val itemKeyList = ArrayList<String>()
    val bookmarkIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment)
        }

        binding.talkTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)

        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)
        }
        getCategoryData()

        myAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        val rv: RecyclerView = binding.mainRV
        rv.adapter = myAdapter
        rv.layoutManager = GridLayoutManager(context, 2)

        return binding.root
    }
    private fun getCategoryData() {
        val event = (object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }
                myAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        FirebaseRef.category1.addValueEventListener(event)
        FirebaseRef.category2.addValueEventListener(event)
    }



}
package com.example.solo_life_app.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.solo_life_app.databinding.FragmentBookmarkBinding
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)


        getBookmarkData()
        myAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        val rv: RecyclerView = binding.bookmarkRv
        rv.adapter = myAdapter
        rv.layoutManager = GridLayoutManager(context, 2)


        // Inflate the layout for this fragment
        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }
        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }
        binding.bookmarkTap.setOnClickListener {
        }
        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)

        }
        return binding.root
    }

    private fun getCategoryData() {
        var flag = true
        val event = (object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(flag) {
                    items.clear()
                    itemKeyList.clear()
                    flag=false
                }
                for (dataModel in dataSnapshot.children) {

                    val item = dataModel.getValue(ContentModel::class.java)
                    if (bookmarkIdList.contains(dataModel.key.toString())) {
                        items.add(item!!)
                        itemKeyList.add(dataModel.key.toString())
                    }

                }
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        FirebaseRef.category1.addValueEventListener(event)
        FirebaseRef.category2.addValueEventListener(event)
    }

    private fun getBookmarkData() {
        FirebaseRef.bookMarkRef.child(FirebaseAuth.getUid())
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    bookmarkIdList.clear()
                    for (dataModel in dataSnapshot.children) {
                        bookmarkIdList.add(dataModel.key.toString())
                    }
                    getCategoryData()
                    myAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


}
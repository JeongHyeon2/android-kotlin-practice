package com.example.solo_life_app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.solo_life_app.R
import com.example.solo_life_app.board.BoardInsideActivity
import com.example.solo_life_app.board.BoardListViewAdapter
import com.example.solo_life_app.board.BoardModel
import com.example.solo_life_app.board.BoardWriteActivity
import com.example.solo_life_app.databinding.FragmentTalkBinding
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TalkFragment : Fragment() {
    private lateinit var binding: FragmentTalkBinding
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var  adpater : BoardListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFBBoardData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)
        binding.writeBtn.setOnClickListener {
            val intent = Intent(context,BoardWriteActivity::class.java)

            startActivity(intent)
        }




        adpater = BoardListViewAdapter(boardDataList)
        binding.boardListview.adapter = adpater
        binding.boardListview.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(context,BoardInsideActivity::class.java)
            intent.putExtra("title",boardDataList[i].title)
            intent.putExtra("content",boardDataList[i].content)
            intent.putExtra("time",boardDataList[i].time)

            startActivity(intent)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }
        binding.talkTap.setOnClickListener {
        }
        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)

        }
        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)

        }
        return binding.root
    }

    private fun getFBBoardData(){
        FirebaseRef.boardRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                }
                boardDataList.reverse()
                adpater.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
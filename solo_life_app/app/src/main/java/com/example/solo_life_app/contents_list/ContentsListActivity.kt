package com.example.solo_life_app.contents_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solo_life_app.R
import com.example.solo_life_app.util.FirebaseAuth
import com.example.solo_life_app.util.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentsListActivity : AppCompatActivity() {
    private lateinit var myRef:DatabaseReference

    var bookmarkIdList = mutableListOf<String>()

    lateinit var myAdapter: RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        val database = Firebase.database
        val category = intent.getStringExtra("category")

        if(category=="category1"){
            myRef = database.getReference("contents")

        }else if(category=="category2"){
            myRef = database.getReference("contents2")
        }
        var items = ArrayList<ContentModel>()
        var itemKeyList = ArrayList<String>()


        myAdapter = RVAdapter(baseContext, items , itemKeyList, bookmarkIdList)

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()
                itemKeyList.clear()
                Log.d("tete","!!!!!!!")
                for (dataModel in dataSnapshot.children) {
                    val item  = dataModel.getValue(ContentModel::class.java)
                   items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }
                myAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = myAdapter
        rv.layoutManager = GridLayoutManager(this, 2)
        getBookmarkData()

    }
    private fun getBookmarkData(){
        FirebaseRef.bookMarkRef.child(FirebaseAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("tete","BookMark!!")
                bookmarkIdList.clear()
                for (dataModel in dataSnapshot.children) {
                    bookmarkIdList.add(dataModel.key.toString())
                }
                myAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
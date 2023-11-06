package com.example.sogating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import android.widget.ListView
import com.example.sogating_app.R
import com.example.sogating_app.auth.UserDataModel
import com.example.sogating_app.utils.FBAuthUtil
import com.example.sogating_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyMsgActivity : AppCompatActivity() {
    lateinit var listAdapter: MsgAdapter
    val msgList = mutableListOf<MsgModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_msg)

        val listView = findViewById<ListView>(R.id.msgListView)
        listAdapter = MsgAdapter(baseContext,msgList)
        listView.adapter = listAdapter
        getMyMsg()
    }
    private fun getMyMsg(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                msgList.clear()
                for(dataModel in dataSnapshot.children ){
                    val msg = dataModel.getValue(MsgModel::class.java)
                    msgList.add(msg!!)
                }
                msgList.reverse()
                listAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dd", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userMsgRef.child(FBAuthUtil.getUid()).addValueEventListener(postListener)
    }
}
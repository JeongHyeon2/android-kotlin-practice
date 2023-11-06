package com.example.sogating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.example.sogating_app.R
import com.example.sogating_app.auth.UserDataModel
import com.example.sogating_app.message.fcm.NotiModel
import com.example.sogating_app.message.fcm.PushNotification
import com.example.sogating_app.message.fcm.RetrofitInstance
import com.example.sogating_app.utils.FBAuthUtil
import com.example.sogating_app.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyLikeListActivity : AppCompatActivity() {
    private val uid = FBAuthUtil.getUid()
    private val likeUserListUid = mutableListOf<String>()
    private val likeUserList = mutableListOf<UserDataModel>()
    private lateinit var listViewAdapter: ListViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        val userListView = findViewById<ListView>(R.id.userListView)
        listViewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listViewAdapter

        getMyLikeList()
        userListView.setOnItemClickListener { adapterView, view, i, l ->
            checkMatching(likeUserList[i].uid)
            val notiModel = NotiModel("aaaa","bbbbb")
            val pushModel = PushNotification(notiModel,likeUserList[i].token)
            testPush(pushModel)
        }
    }

    // 내가 좋아요한 리스트
    private fun getMyLikeList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    likeUserListUid.add(dataModel.key.toString())

                }
                getUserList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dd", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userLikeRef.child(uid).addValueEventListener(postListener)

    }

    private fun getUserList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if (likeUserListUid.contains(user?.uid)) {
                        likeUserList.add(user!!)

                    }
                }
                listViewAdapter.notifyDataSetChanged()
                Log.d("qwlqwl", likeUserList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dd", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userInfoRef.addValueEventListener(postListener)
    }

    private fun checkMatching(otherUid: String) {
        var isMatching = false
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    if (dataModel.key.toString() == uid) {
                        Toast.makeText(baseContext, "매칭되었습니다!!", Toast.LENGTH_SHORT).show()
                        isMatching = true

                    }
                }
                if(!isMatching){
                    Toast.makeText(baseContext, "매칭이 되지 않았습니다!!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dd", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }
    private fun testPush(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
        Log.d("gigigi","abcd")
        RetrofitInstance.api.postNotification(notification)
        Log.d("gigigi","fffff")
    }
}
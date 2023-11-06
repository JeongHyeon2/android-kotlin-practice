package com.example.sogating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sogating_app.R
import com.example.sogating_app.auth.UserDataModel
import com.example.sogating_app.message.fcm.NotiModel
import com.example.sogating_app.message.fcm.PushNotification
import com.example.sogating_app.message.fcm.RetrofitInstance
import com.example.sogating_app.utils.FBAuthUtil
import com.example.sogating_app.utils.FBRef
import com.example.sogating_app.utils.MyInfo
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
    lateinit var getterUid : String
    lateinit var getterToken : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)

        val userListView = findViewById<ListView>(R.id.userListView)
        listViewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listViewAdapter

        getMyLikeList()
        userListView.setOnItemClickListener { adapterView, view, i, l ->
            checkMatching(likeUserList[i].uid)
            getterUid = likeUserList[i].uid
            getterToken = likeUserList[i].token


        }
        userListView.setOnItemLongClickListener { adapterView, view, i, l ->
            getterUid = likeUserList[i].uid
            showDialog()

            return@setOnItemLongClickListener(true)
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
                        showDialog()
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
        RetrofitInstance.api.postNotification(notification)
    }

    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("메세지 보내기")
      val mAlertDialog =   mBuilder.show()

        val btn = mAlertDialog.findViewById<Button>(R.id.sendBtn)
        val text = mAlertDialog.findViewById<EditText>(R.id.sendTextArea)
        btn?.setOnClickListener {

            val msgModel = MsgModel(text!!.text.toString(),MyInfo.myNickname)
            FBRef.userMsgRef.child(getterUid).push().setValue(msgModel)

            val notiModel = NotiModel(MyInfo.myNickname,text!!.text.toString())
            val pushModel = PushNotification(notiModel,getterToken)
            testPush(pushModel)

            mAlertDialog.dismiss()

        }

    }


}
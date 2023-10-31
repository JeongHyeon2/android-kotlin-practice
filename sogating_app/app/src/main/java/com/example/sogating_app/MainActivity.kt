package com.example.sogating_app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.sogating_app.auth.IntroActivity
import com.example.sogating_app.auth.UserDataModel
import com.example.sogating_app.setting.MyPageActivity
import com.example.sogating_app.setting.SettingActivity
import com.example.sogating_app.slider.CardStackAdapter
import com.example.sogating_app.utils.FBAuthUtil
import com.example.sogating_app.utils.FBRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.ktx.storage
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {
    private lateinit var cardStackAdapter: CardStackAdapter
    private lateinit var manager : CardStackLayoutManager
    private val userDataList = mutableListOf<UserDataModel>()
    private var userCount = 0
    private lateinit var currentUserGender :String
    private lateinit var uid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        uid = FBAuthUtil.getUid()
        findViewById<ImageView>(R.id.setting_icon).setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        val cardStackView = findViewById<CardStackView>(R.id.card_stack_view)


        manager = CardStackLayoutManager(baseContext,object:CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {
                userCount++

                if(direction==Direction.Right){
                    userLikeOtherUser(uid,userDataList[userCount-1].uid)

                }
                if(direction==Direction.Left){

                }
                if(userCount==userDataList.count()){
                    userCount = 0
                    getUserList(currentUserGender)
                    Toast.makeText(baseContext,"새로운 유저를 받아왔습니다.",Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })

        cardStackAdapter = CardStackAdapter(baseContext,userDataList)

        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        getMyData()


    }
    private fun getUserList(currentUserGender : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userDataList.clear()
                for(dataModel in dataSnapshot.children ){
                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(user!!.gender!=currentUserGender) {
                        userDataList.add(user!!)
                    }
                }
                cardStackAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dd", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userInfoRef.addValueEventListener(postListener)
    }
    private fun getMyData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user  = dataSnapshot.getValue(UserDataModel::class.java)
                currentUserGender = user!!.gender
                getUserList(currentUserGender)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        FBRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }
    private fun userLikeOtherUser(uid:String,otherUid:String){
        FBRef.userLikeRef.child(uid).child(otherUid).setValue("true")
        getOtherUserLikeList(otherUid)
    }
    private fun getOtherUserLikeList(otherUid:String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               for(dataModel in dataSnapshot.children){
                   if(dataModel.key.toString()==uid){
                       Toast.makeText(baseContext,"매칭!!!",Toast.LENGTH_SHORT).show()
                       createNotificationChannel()
                       sendNotification()
                   }

               }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dd", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TestChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        var builder = NotificationCompat.Builder(this, "TestChannel")
            .setSmallIcon(R.drawable.ok)
            .setContentTitle("알림!!")
            .setContentText("매칭 성공!")
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(
                    baseContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            notify(123, builder.build())
        }
    }

}
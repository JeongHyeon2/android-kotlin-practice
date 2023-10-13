package com.example.diet_memo

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : AppCompatActivity() {
    val dataModelList = mutableListOf<DataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = Firebase.database
        val myRef = database.getReference("myMemo")
        val listview = findViewById<ListView>(R.id.mainLV)
        val myAdapter = ListViewAdpater(dataModelList)

        listview.adapter = myAdapter

        myRef.child(Firebase.auth.currentUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataModelList.clear()
                for (dataModel in snapshot.children){
                    dataModelList.add(dataModel.getValue(DataModel::class.java)!!)
                }
                myAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        val writeBtn = findViewById<ImageView>(R.id.writeBtn)
        writeBtn.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("운동 메모 다이얼로그")
            val mAlertDialog = mBuilder.show()
            val selectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)!!
            var dateText = ""

            selectBtn.setOnClickListener {
                val today = GregorianCalendar()

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                        selectBtn.text = ("${p1}년 ${p2 + 1}월 ${p3}일")
                        dateText = "${p1}년 ${p2 + 1}월 ${p3}일"
                    }

                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE))
                dlg.show()

            }
            val saveBtn = mAlertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {
                val healthMemo = mAlertDialog.findViewById<EditText>(R.id.healthMemo)!!.text.toString()
                val database = Firebase.database
                val myRef = database.getReference("myMemo").child(Firebase.auth.currentUser!!.uid)

               val model = DataModel(dateText,healthMemo)

                myRef.push().setValue(model)

                mAlertDialog.dismiss()
            }
        }
    }
}
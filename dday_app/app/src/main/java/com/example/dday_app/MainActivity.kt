package com.example.dday_app

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import java.util.Calendar
import java.util.GregorianCalendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnEnd = findViewById<Button>(R.id.btn_end)

        var startDate = ""
        var endDate = ""
        val tv_dday = findViewById<TextView>(R.id.tv_dday)


        btnStart.setOnClickListener {

            val today = GregorianCalendar()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    startDate = "${p1}${(p2+1).toString().padStart(2,'0')}${p3.toString().padStart(2,'0')}"
                    Log.e("ddddd",startDate)
                }


            }, year,month, day)
            dlg.show()

        }
        btnEnd.setOnClickListener {
            val today = GregorianCalendar()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    endDate = "${p1}${(p2+1).toString().padStart(2,'0')}${p3.toString().padStart(2,'0')}"
                    Log.e("ddddd",startDate)
                    tv_dday.text = (endDate.toInt()-startDate.toInt()+1).toString()
                }


            }, year,month, day)
            dlg.show()
        }

    }
}
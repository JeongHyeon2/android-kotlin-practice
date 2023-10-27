package com.example.solo_life_app.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.solo_life_app.R
import com.example.solo_life_app.util.FirebaseAuth

class BoardListViewAdapter (val boardList:MutableList<BoardModel>): BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(p0: Int): Any {
       return boardList[p0]
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
       var convertView = p1

            convertView = LayoutInflater.from(p2?.context).inflate(R.layout.board_list_item,p2,false)

        val itemLinearLayout = convertView!!.findViewById<LinearLayout>(R.id.itemview)
        val title = convertView!!.findViewById<TextView>(R.id.tv_title)
        val content = convertView!!.findViewById<TextView>(R.id.tv_content)
        val time = convertView!!.findViewById<TextView>(R.id.tv_time)
        title.text = boardList[p0].title
        content.text = boardList[p0].content
        time.text = boardList[p0].time
        if(boardList[p0].uid.equals(FirebaseAuth.getUid())){
            itemLinearLayout.setBackgroundColor(Color.parseColor("#ffa500"))
        }

        return convertView!!
    }
}
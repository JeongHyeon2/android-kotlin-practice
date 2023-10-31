package com.example.sogating_app.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.sogating_app.R
import com.example.sogating_app.auth.UserDataModel

class ListViewAdapter(val context: Context, val items : MutableList<UserDataModel>) :BaseAdapter(){
    override fun getCount(): Int {
      return items.size
    }

    override fun getItem(p0: Int): Any {
      return items[p0]
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
       var view = p1
        if(view==null){
            view = LayoutInflater.from(p2?.context).inflate(R.layout.listview_item,p2,false)
        }
        view!!.findViewById<TextView>(R.id.listview_item_nickname).text = items[p0].nickname

        return view!!
    }
}
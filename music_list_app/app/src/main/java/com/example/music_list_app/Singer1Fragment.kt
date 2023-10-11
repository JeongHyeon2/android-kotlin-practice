package com.example.music_list_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController

class Singer1Fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_singer1, container, false)
        view.findViewById<ImageView>(R.id.image1).setOnClickListener {  }
        view.findViewById<ImageView>(R.id.image2).setOnClickListener {  it.findNavController().navigate(R.id.action_singer1Fragment_to_singer2Fragment)}
        view.findViewById<ImageView>(R.id.image3).setOnClickListener {  it.findNavController().navigate(R.id.action_singer1Fragment_to_singer3Fragment)}

        return view
    }

}
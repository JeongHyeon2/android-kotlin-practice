package com.example.cooking_app.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.R
import com.example.cooking_app.databinding.FragmentSettingBinding
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import com.example.cooking_app.views.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: MyRecipeFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MyRecipeFragmentViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.homeFragmentLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
            builder.setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        FBAuth.logout()
                        startActivity(intent)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.show()

        }
        val user = Firebase.auth.currentUser
        user?.let {
            binding.homeFragmentId.text   = "ID: "+ it.email
        }
        binding.homeFragmentSaveDataToFb.setOnClickListener {
            viewModel.saveDataToFB()
        }
        binding.homeFragmentGetDataFromFb.setOnClickListener {
            viewModel.getDataFromFB()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
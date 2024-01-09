package com.example.cooking_app.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.cooking_app.R
import com.example.cooking_app.databinding.FragmentSettingBinding
import com.example.cooking_app.utils.FBAuth
import com.example.cooking_app.viewmodels.MyRecipeFragmentViewModel
import com.example.cooking_app.views.IntroActivity
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
                        val intent = Intent(requireContext(), IntroActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        FBAuth.logout()
                        startActivity(intent)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            alertDialog.show()

        }
        val user = Firebase.auth.currentUser
        user?.let {
            binding.homeFragmentId.text   = "ID: "+ it.email
        }
        binding.homeFragmentSaveDataToFb.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
            builder.setTitle("데이터 저장하기")
                .setMessage("서버에 현재 내 폰에 있는 데이터를 저장합니다.\n기존 서버에 저장된 데이터는 삭제됩니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.saveDataToFB()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            alertDialog.show()

        }
        binding.homeFragmentGetDataFromFb.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
            builder.setTitle("데이터 가져오기")
                .setMessage("서버로부터 데이터를 가져옵니다.\n현재 내 폰에 저장된 데이터는 삭제됩니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.getDataFromFB()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            alertDialog.show()

        }
        binding.getHint.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://jeonghyeon2.notion.site/71bad9f6830c411bb1473e11bd70b4ca?pvs=4"))
            startActivity(intent)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
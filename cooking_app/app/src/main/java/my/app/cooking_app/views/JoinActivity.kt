package my.app.cooking_app.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import my.app.cooking_app.databinding.ActivityJoinBinding
import my.app.cooking_app.viewmodels.JoinViewModel


class JoinActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinBinding
    private lateinit var viewModel : JoinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, my.app.cooking_app.R.layout.activity_join)

        viewModel = ViewModelProvider(this)[JoinViewModel::class.java]
        binding.vm = viewModel


    }

}
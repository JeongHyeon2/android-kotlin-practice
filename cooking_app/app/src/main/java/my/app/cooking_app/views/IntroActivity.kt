package my.app.cooking_app.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import my.app.cooking_app.R
import my.app.cooking_app.databinding.ActivityIntroBinding
import my.app.cooking_app.viewmodels.IntroViewModel

class IntroActivity : AppCompatActivity() {
    private val viewModel : IntroViewModel by viewModels()
    private lateinit var binding : ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        binding.introBtnLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))

        }
        binding.introBtnJoin.setOnClickListener {
            startActivity(Intent(this,JoinActivity::class.java))
        }

        viewModel.first.observe(this, Observer {
            if(it){
                //처음 접속한 유저가 아니면
                startActivity(Intent(this,MainActivity::class.java))
            }
        })
    }
}
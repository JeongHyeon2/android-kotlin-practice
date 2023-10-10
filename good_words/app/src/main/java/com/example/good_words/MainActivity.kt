package com.example.good_words

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.good_words.databinding.ActivityMainBinding

val sentenceList = mutableListOf<String>(
    "검정화면에 대충 흰글씨 쓰면 명언같다.",
    "자기 아이에게 육체적 노동을 가르치지 않는 것은 약탈과 강도를 가르치는 것과 마찬가지다.",
    "결점이 없는 친구를 사귀려고 한다면 평생 친구를 가질 수 없을 것이다.",
    "사람에게 하나의 입과 두 개의 귀가 있는 것은 말하기보다 듣기를 두 배로 하라는 뜻이다.",
    "검정화면에 대충 흰글씨 쓰면 명언같다.",
    "자기 아이에게 육체적 노동을 가르치지 않는 것은 약탈과 강도를 가르치는 것과 마찬가지다.",
    "결점이 없는 친구를 사귀려고 한다면 평생 친구를 가질 수 없을 것이다.",
    "사람에게 하나의 입과 두 개의 귀가 있는 것은 말하기보다 듣기를 두 배로 하라는 뜻이다.",
    "검정화면에 대충 흰글씨 쓰면 명언같다.",
    "자기 아이에게 육체적 노동을 가르치지 않는 것은 약탈과 강도를 가르치는 것과 마찬가지다.",
    "결점이 없는 친구를 사귀려고 한다면 평생 친구를 가질 수 없을 것이다.",
    "사람에게 하나의 입과 두 개의 귀가 있는 것은 말하기보다 듣기를 두 배로 하라는 뜻이다.",
    "검정화면에 대충 흰글씨 쓰면 명언같다.",
    "자기 아이에게 육체적 노동을 가르치지 않는 것은 약탈과 강도를 가르치는 것과 마찬가지다.",
    "결점이 없는 친구를 사귀려고 한다면 평생 친구를 가질 수 없을 것이다.",
    "사람에게 하나의 입과 두 개의 귀가 있는 것은 말하기보다 듣기를 두 배로 하라는 뜻이다.",
    "검정화면에 대충 흰글씨 쓰면 명언같다.",
    "자기 아이에게 육체적 노동을 가르치지 않는 것은 약탈과 강도를 가르치는 것과 마찬가지다.",
    "결점이 없는 친구를 사귀려고 한다면 평생 친구를 가질 수 없을 것이다.",
    "사람에게 하나의 입과 두 개의 귀가 있는 것은 말하기보다 듣기를 두 배로 하라는 뜻이다.",
    "검정화면에 대충 흰글씨 쓰면 명언같다.",
    "자기 아이에게 육체적 노동을 가르치지 않는 것은 약탈과 강도를 가르치는 것과 마찬가지다.",
    "결점이 없는 친구를 사귀려고 한다면 평생 친구를 가질 수 없을 것이다.",
    "사람에게 하나의 입과 두 개의 귀가 있는 것은 말하기보다 듣기를 두 배로 하라는 뜻이다.",
)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.showFullSentenceBtn.setOnClickListener {
            val intent = Intent(this, SentenceActivity::class.java)
            startActivity(intent)
        }
        binding.goodWordTextArea.text = sentenceList.random()
    }
}
package com.example.mytest2024

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mytest2024.databinding.SplashActivityBinding
import com.example.mytest2024.firebase.PushService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {



    private lateinit var binding: SplashActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 타이머가 끝나면 내부 실행
        //TODO("Coroutine으로 변경해보기")

//        Handler().postDelayed(Runnable {
//            val i = Intent(this@SplashActivity, LoginActivity::class.java)
//            i.apply {
//                startActivity(i)
//                finish()
//
//            }
//
//            // 현재 액티비티 닫기
//        }, 2000)


        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@SplashActivity,LoginActivity::class.java)
            intent.apply {
                startActivity(intent)
                finish()
            }
        }



    }
}
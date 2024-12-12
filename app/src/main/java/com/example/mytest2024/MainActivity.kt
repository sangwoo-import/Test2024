package com.example.mytest2024

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.mytest2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.fragmentBtn1.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FirstFragment())
                commit() // 모든 명령이 추가 되었다고 알린다.
            }
        }

        binding.fragmentBtn2.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, SecondFragment())
                commit()
            }
        }

//        binding.homeBtn.setOnClickListener {
//            onBackPressed()
//
//        }




    }

    interface onBackPressedListner {

        fun onBackPressed()
    }

    override fun onBackPressed(){
        super.onBackPressed()

        val fragmentList = supportFragmentManager.fragments

        for(fragment in fragmentList){
            if (fragment is onBackPressedListner){
                (fragment as onBackPressedListner).onBackPressed()
                return
            }
        }

    }


}
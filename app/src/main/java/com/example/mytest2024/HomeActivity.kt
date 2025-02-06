package com.example.mytest2024

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.mytest2024.databinding.HomeActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding

    /*뒤로 가기 했을 때 한번더 누를 시간 */
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 백 버튼 처리
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_container)

                // 🔥 현재 Fragment가 NewsFragment라면 WebView에서 뒤로 가기 우선 처리
                if (currentFragment is NewsFragment && currentFragment.canGoBack()) {
                    currentFragment.goBack()
                    return
                }

                // ✅ 공통 백 버튼 로직 (모든 Fragment에서 동일하게 작동)
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@HomeActivity, "앱 종료를 위해 한 번 더 누르세요!", Toast.LENGTH_SHORT)
                        .show()
                    backPressedTime = System.currentTimeMillis()
                }

            }
        })


        // 처음 시작 할 때 불러오는 프래그 먼트 위치
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, HomeFragment()).commit()


        }


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment()).commit()
                    true
                }

                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment()).commit()
                    true
                }

                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, NewsFragment()).commit()
                    true
                }

                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MyFragment()).commit()
                    true
                }

                else -> false
            }
        }


    }
}
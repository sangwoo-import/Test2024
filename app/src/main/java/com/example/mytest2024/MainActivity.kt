package com.example.mytest2024

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources.Theme
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.mytest2024.Controller.ImageProvider
import com.example.mytest2024.Model.ImageCountModel
import com.example.mytest2024.RecyclerView.RecyclerViewActivity

import com.example.mytest2024.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val model = ImageCountModel()
    // private val imageProvider = ImageProvider(this)

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /* SharedPreference 사용 */
        val pref: SharedPreferences = getSharedPreferences("test", MODE_PRIVATE)
        val editor = pref.edit()


//        binding.fragmentBtn1.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragment_container, FirstFragment())
//                addToBackStack(null)
//                commit() // 모든 명령이 추가 되었다고 알린다.
//            }
//
//        }
//
//        binding.fragmentBtn2.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragment_container, SecondFragment())
//                addToBackStack(null)
//                commit()
//            }
//        }
//
//        binding.homeBtn.setOnClickListener {
//            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//
//        }

        binding.loginBtn.setOnClickListener {

            if (binding.idEditText.text.isNotEmpty()) {
                editor.putString("test1", "${binding.idEditText.text}")
                editor.apply()
            } else {
                Toast.makeText(this@MainActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.apply {
                startActivity(intent)
            }


        }




    }

//    fun loadImage() {
//
//        imageProvider.getRandomImage()
//    }
//
//    override fun loadImage(url: String) {
//
//        val intent = Intent(this@MainActivity, RecyclerViewActivity::class.java)
//        intent.putExtra("key", url)
//        startActivity(intent)
//
//    }


}
package com.example.mytest2024.RecyclerView

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytest2024.Controller.ImageProvider
import com.example.mytest2024.MainActivity
import com.example.mytest2024.Model.ImageCountModel
import com.example.mytest2024.R
import com.example.mytest2024.databinding.RecyclerviewBinding
import okhttp3.internal.notify
import java.lang.System.load

class RecyclerViewActivity : AppCompatActivity(), ImageProvider.CallBack {
    private val imageProvider = ImageProvider(this)


    private lateinit var binding: RecyclerviewBinding


    private var address = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImage()
    }


    fun loadImage() {
        imageProvider.getRandomImage()
    }


    override fun loadImage(url: String) {
        address = url

        // RecyclerView에서 직접 받을려면
        // 데이터를 콜백 받는 메서드이기 때문에 비동기처리가 이루어지기 때문에 onCreate에서 받으면 안되고
        // 이 함수에서 데이터 셋을 지정해서 만들고 여기서 호출되는 데이터를 recyclerView Adapter 연결
        val dataSet = arrayListOf(
            RecyclerViewData(address, "1번"),
            RecyclerViewData(address, "2번"),
            RecyclerViewData(address, "3번"),
            RecyclerViewData(address, "4번"),
            RecyclerViewData(address, "5번"),
            RecyclerViewData(address, "6번"),
            RecyclerViewData(address, "7번"),
            RecyclerViewData(address, "8번"),
            RecyclerViewData(address, "9번"),
            RecyclerViewData(address, "10번"),
            RecyclerViewData(address, "11번"),
            RecyclerViewData(address, "12번"),
            RecyclerViewData(address, "13번"),
            RecyclerViewData(address, "14번"),
            RecyclerViewData(address, "15번"),
            RecyclerViewData(address, "16번"),
            RecyclerViewData(address, "17번"),
            RecyclerViewData(address, "18번"),
            RecyclerViewData(address, "19번"),
            RecyclerViewData(address, "20번")
        )

        val recyclerView = binding.recyclerView
        // LinearLayoutManager가 RecyclerViewActivity를 통제한다는 의미    LinearLayout 속성에 수직으로 정했으니 수직으로 스크롤 한다.
        recyclerView.layoutManager = LinearLayoutManager(this@RecyclerViewActivity)
        // adapter 붙어주기에 만든 data를 붙여주기 + context
        recyclerView.adapter = RecyclerViewAdapter(dataSet, this@RecyclerViewActivity)

    }


}
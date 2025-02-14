package com.example.mytest2024

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.mytest2024.databinding.ActivitySettingBinding
import com.example.mytest2024.screenlock.BioAuthManager


class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding


    private var state = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val sharedPref = getSharedPreferences("LockCheck", Context.MODE_PRIVATE)

        state = sharedPref.getBoolean("check", false)
        binding.appLockSwitch.isChecked = state  // UI 업데이트

        /* TODO (SharedPreference 적용해야지  유지 될 듯?? */
        binding.appLockSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                state = isChecked
                with(sharedPref.edit()) {
                    putBoolean("check", isChecked)
                    apply()
                }

            } else {
                with(sharedPref.edit()) {
                    remove("check")
                    apply()
                }


            }
        }




    }


}
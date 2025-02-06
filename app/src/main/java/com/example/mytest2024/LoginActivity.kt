package com.example.mytest2024

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mytest2024.SwaggerAPI.DeviceInformation.DeviceInfo
import com.example.mytest2024.SwaggerAPI.SwaggerController.LoginProvider
import com.example.mytest2024.SwaggerAPI.LoginUserInformation
import com.example.mytest2024.SwaggerAPI.Retrofit.LoginRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LoginResponse
import com.example.mytest2024.databinding.LoginActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), LoginProvider.CallBack {


    var backPressedTime: Long = 0   // 뒤로 가기 했을 때 한번더 누를 시간
    private val loginProvider = LoginProvider(this)

    private var isPassWord = true  // 기본 pw 숨기기 imageButton 색상


    // response data : code, msg, userName, wardNm, classNm, userSn
    private var codeA: String = ""
    private var messageA: String = ""
    var userNameA: String = ""
    var wardNmA: String = ""
    var classNmA: String = ""
    var userSnA: String = ""


    private lateinit var binding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deviceInformation = DeviceInfo(this@LoginActivity)

        // device id, os, device name 정보 받기
        val deviceId = deviceInformation.getDeviceId()
        val deviceOs = deviceInformation.getDeviceOs()
        val deviceName = deviceInformation.getDeviceModel()

        /* Device 정보 저장 */
        LoginUserInformation.saveDeviceInfo(deviceId)


        /* 뒤로가기 */
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            // 이 기능은 내장된 뒤로 가기 버튼을 누르면 자동으로 호출됩니다
            override fun handleOnBackPressed() {
                //두 번 연속 뒤로 버튼을 누르는 사이의 경과 시간이 3초 미만인지 확인합니다.
                // 3초 미만 일때 한번 더 누르면 앱 종료
                if (backPressedTime + 3000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "앱 종료를 위해 한번더 누르세요!", Toast.LENGTH_LONG)
                        .show()
                }
                backPressedTime = System.currentTimeMillis()
            }

        }
        onBackPressedDispatcher.addCallback(this, callback)


        /* 비밀 번호 숨기기 보이기*/
        binding.hideAndShowImageButton.setOnClickListener {
            if (isPassWord) {
                // 패스워드 숨김 -> 기본
                binding.pwEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.hideAndShowImageButton.setImageResource(R.drawable.visibility_lock_24px)
            } else {
                binding.pwEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.hideAndShowImageButton.setImageResource(R.drawable.visibility_lock2)
            }
            // 커서 위치 유지
            binding.pwEditText.setSelection(binding.pwEditText.text.length)
            //상태 토글
            isPassWord = !isPassWord
        }


        /*작업을 위해 아이디 비밀번호 자동 완성 하게 만듦*/
        binding.loginLogoImage.setOnClickListener {
            binding.idEditText.setText("winitest")
            binding.pwEditText.setText("winitech0)")
        }

        binding.loginBtn.setOnClickListener {

            // request 값
            val loginRequestDataSet = LoginRequestData(
                "${binding.idEditText.text}",
                "${binding.pwEditText.text}",
                "${deviceOs}",
                "${deviceName}",
                "test",
                "${deviceId}"

            )
            requestLogin(loginRequestDataSet)
            binding.loginBtn.isEnabled = false


        }


    }

    fun requestLogin(loginRequestDataSet: LoginRequestData) {
        loginProvider.sendLogin(loginRequestDataSet)
    }

    // response call back 값
    override fun completeLogin(
        code: String,
        msg: String,
        data: List<LoginResponse>
    ) {
        codeA = code
        messageA = msg

        if (codeA.equals("100")) {

            for (dataItem in data) {
                userSnA = dataItem.userSn
                userNameA = dataItem.userName
                wardNmA = dataItem.wardNm
                classNmA = dataItem.classNm
            }

            /* 싱글톤 패턴 사용자 정보 담기 */
            LoginUserInformation.saveUserInfo(userNameA, wardNmA, classNmA, userSnA)

            binding.progressBarLinearLayout.visibility = View.VISIBLE
            /*이미지 로딩*/
//            Glide.with(this@LoginActivity).load("file:///android_asset/loading_animation.gif")
//                .into(binding.progressImageView)

            lifecycleScope.launch {
                delay(2000)
                Toast.makeText(this@LoginActivity, "로그인의" + messageA + "했습니다!", Toast.LENGTH_SHORT)
                    .show()

                binding.progressBarLinearLayout.visibility = View.GONE
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.apply {
                    startActivity(intent)
                    finish()
                }

            }

        } else if (codeA.equals("E100")) {
            Log.d("error!!", codeA + ": " + messageA)
            binding.loginBtn.isEnabled = true
            Toast.makeText(this@LoginActivity, messageA, Toast.LENGTH_SHORT)
                .show()

        } else if (codeA.equals("E600")) {
            Log.d("error!!", codeA + ": " + messageA)
            binding.loginBtn.isEnabled = true
            Toast.makeText(this@LoginActivity, messageA, Toast.LENGTH_SHORT)
                .show()
        } else {
            Log.d("error!!", codeA + ": " + messageA)
            binding.loginBtn.isEnabled = true
            Toast.makeText(this@LoginActivity, messageA, Toast.LENGTH_SHORT)
                .show()

        }


    }


}
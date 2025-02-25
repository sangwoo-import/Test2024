package com.example.mytest2024.screenlock

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import kotlin.math.log


class BioAuthManager {
    private val REQUEST_FINGERPRINT_ENROLLMENT_AUTH = 10

    private var keyManager: KeyManager? = null

    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null

    private var fingerprintManager: FingerprintManager? = null
    private var keyguardManager: android.app.KeyguardManager? = null

    private var cryptoObject: FingerprintManager.CryptoObject? = null // for API 23+
    private var bioCryptoObject: BiometricPrompt.CryptoObject? = null // for API 28+

    companion object {
        @Volatile
        private var instance: BioAuthManager? = null

        fun getInstance(): BioAuthManager {
            return instance ?: synchronized(this) {
                instance ?: BioAuthManager().also { instance = it }
            }
        }
    }

    private fun canAuthenticate(activity: Activity, context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                    return true
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")
                    return false
                }

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                    return false
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    Log.d("MY_APP_TAG", "Enrolled biometric doesn't exist. Please enroll.")

                    val intent = Intent(Settings.ACTION_FINGERPRINT_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                    BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_WEAK
                        )
                    }
                    activity.startActivityForResult(intent, REQUEST_FINGERPRINT_ENROLLMENT_AUTH)
                    return false
                }
            }
        } else if (Build.VERSION.SDK_INT in Build.VERSION_CODES.M until Build.VERSION_CODES.P) {
            fingerprintManager =
                context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            keyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as android.app.KeyguardManager

            return when {
                // 지문을 사용할 수 없는 디바이스인 경우

                fingerprintManager?.isHardwareDetected == false -> {
                    Log.d("fingerprint", "Device does not support fingerprint authentication")
                    false
                }
                // 지문 인증 사용을 거부한 경우

                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.USE_FINGERPRINT
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                    Log.d("fingerprint", "Permission denied")
                    false
                }
                // 잠금 화면이 설정되지 않은 경우

                keyguardManager?.isKeyguardSecure == false -> {
                    Log.d("fingerprint", "Please set lock screen")
                    false
                }
                // 등록된 지문이 없는 경우

                fingerprintManager?.hasEnrolledFingerprints() == false -> {
                    Log.d("fingerprint", "No enrolled fingerprints. Please enroll.")
                    val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                    activity.startActivityForResult(intent, REQUEST_FINGERPRINT_ENROLLMENT_AUTH)
                    false
                }

                else -> true
            }
        }
        return false
    }

    fun authenticate(activity: FragmentActivity, context: Context) {
        keyManager = KeyManager.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.d("bioAuth", "Starting biometric authentication")

            if (canAuthenticate(activity, context)) {
                executor = ContextCompat.getMainExecutor(context)
                biometricPrompt = BiometricPrompt(
                    activity,
                    executor!!,
                    object : BiometricPrompt.AuthenticationCallback() {
                        /*인증실패*/
                        override fun onAuthenticationError( // 취소 버튼 이나 뒤로가기 버튼 눌렀을 때 취소 처리
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            Log.d("bioAuth", errString.toString() + " " + errorCode.toString())


                            /* 다시 띄우기*/
                            /*취소 버튼 눌렀을 때*/
                            if(errString.equals("취소")){
                                activity.moveTaskToBack(true)

                            }

                            /* 뒤로가기 버튼 눌렀을 때 or 백그라운드로 갈 때*/
                            if (errString.equals("인증이 취소되었습니다.")) {
                                activity.moveTaskToBack(true)
                            }
                            /*입력 많이 눌러서 나오는 거 */
                            if (errString.equals("시도 횟수가 너무 많습니다. 나중에 다시 시도하세요.")) {
                                activity.finish()


                            }
                        }

                        /* 인증 성공*/
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            Log.d("bioAuth", "인증 성공")
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Log.d("bioAuth", "Authentication failed")

                        }
                    })

                /* PromptInfo 생성*/
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("지문 인증")
                    .setSubtitle("기기에 등록된 지문을 이용하여 지문을 인증해주세요.")
                    .setDescription("생체 인증 설명")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .setConfirmationRequired(false)
//                    .setDeviceCredentialAllowed(true) // PIN, 패턴, 비밀번호 허용 Android 10 이상
                    .setNegativeButtonText("취소")
                    .build()


                keyManager?.generateKey()
                if (keyManager?.cipherInit() == true) {
                    bioCryptoObject = BiometricPrompt.CryptoObject(keyManager!!.cipher!!)
                    biometricPrompt?.authenticate(promptInfo!!, bioCryptoObject!!)
                } else {
                    biometricPrompt?.authenticate(promptInfo!!)
                }
            }
        } else if (Build.VERSION.SDK_INT in Build.VERSION_CODES.M until Build.VERSION_CODES.P) {
            Log.d("fingerprint", "Starting fingerprint authentication")

            fingerprintManager =
                context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            keyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as android.app.KeyguardManager

            if (canAuthenticate(activity, context)) {
                Log.d("fingerprint", "All fingerprint authentication requirements passed")

                keyManager?.generateKey()
                if (keyManager?.cipherInit() == true) {
                    val cipher = keyManager!!.cipher!!
                    cryptoObject = FingerprintManager.CryptoObject(cipher)

                    fingerprintManager?.authenticate(
                        cryptoObject,
                        CancellationSignal(),
                        0,
                        object : FingerprintManager.AuthenticationCallback() {
                            override fun onAuthenticationError(
                                errorCode: Int,
                                errString: CharSequence
                            ) {
                                super.onAuthenticationError(errorCode, errString)
                                Log.d("fingerprint", errorCode.toString())
                            }

                            override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                Log.d("fingerprint", "Authentication successful")
                            }

                            override fun onAuthenticationFailed() {
                                super.onAuthenticationFailed()
                                Log.d("fingerprint", "Authentication failed")
                            }

                            override fun onAuthenticationHelp(
                                helpCode: Int,
                                helpString: CharSequence
                            ) {
                                super.onAuthenticationHelp(helpCode, helpString)
                                Log.d("fingerprint", helpString.toString())
                            }
                        },
                        null
                    )
                }
            }
        }
    }

}
package com.example.mytest2024.screenlock

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class KeyManager  {

    var cipher: Cipher? = null
    var signal = android.os.CancellationSignal()

    companion object {
        private var instance: KeyManager? = null

        fun getInstance(): KeyManager {
            if (instance == null) {
                instance = KeyManager()
            }
            return instance!!
        }
    }

    private val keyName = "BIOAUTH_KEY"
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator

    /**
     * 지문 인증을 사용하기 위한 키를 생성하는 함수
     */
    fun generateKey() {
        try {
            // 안드로이드에서 기본적으로 제공하는 KeyStore 인듯하다 ( AndroidKeyStore )
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            keyStore.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    keyName,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            )
            keyGenerator.generateKey()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 키스토어에 저장된 키를 암호화하는 함수
     * @return Boolean
     */
    fun cipherInit(): Boolean {
        return try {
            cipher = Cipher.getInstance(
                "${KeyProperties.KEY_ALGORITHM_AES}/" +
                        "${KeyProperties.BLOCK_MODE_CBC}/" +
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
            )

            val key = keyStore.getKey(keyName, null) as SecretKey
            cipher?.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}



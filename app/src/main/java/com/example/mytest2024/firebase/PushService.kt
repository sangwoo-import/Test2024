package com.example.mytest2024.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.content.edit
import com.example.mytest2024.LoginActivity
import com.example.mytest2024.MainActivity
import com.example.mytest2024.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.SharedFlow

class PushService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        /* 토큰 값 따로 저장 */
        val pref = getSharedPreferences("token", Context.MODE_PRIVATE)

        with(pref.edit()) {
            putString("token", token).apply()
        }
        sendRegistrationToServer(token)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            Log.i("바디:", message.data["body"].toString())
            Log.i("타이틀:", message.data["title"].toString())
            sendNotification(message)
        }
    }

    // 타사 서버에 토큰을 유지해주는 메서드이다.
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG , "sendRegistrationTokenToServer($token)")
    }


    private fun sendNotification(message: RemoteMessage) {

        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임한다.
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남긴다. A-B-C-D-B => A-B
        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 채널 이름
        val channelId = getString(R.string.firebase_notification_channel_id)

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정한다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setContentTitle(message.data["body"].toString()) // 제목
            .setContentText(message.data["title"].toString()) // 메시지 내용
            .setAutoCancel(true)
            .setChannelId(channelId)
            .setVisibility(VISIBILITY_PRIVATE)
            .setSound(soundUri) // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent
            //.setFullScreenIntent(pendingIntent,true)  // 앱 잠금화면 강제 깨우기


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        // 오레오 버전 이후에는 채널이 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH).apply {
                    setShowBadge(true)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC

                }
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())

        wakeScreen()


    }



    private fun wakeScreen() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "MyApp:WakeLock"
        ).apply {
            acquire(2000)  // 🔹 3초 동안 화면 깨우기
        }

    }




}

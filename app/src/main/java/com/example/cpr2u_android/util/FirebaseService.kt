package com.example.cpr2u_android.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.cpr2u_android.R
import com.example.cpr2u_android.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import timber.log.Timber
import java.util.Date

class FirebaseService : FirebaseMessagingService() {
    var jsonType: String = ""
    override fun onNewToken(token: String) {
        Log.d("MyFcmService", "New token :: $token")
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // TODO :: TOKEN 값을 서버에 저장
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO :: 전달받은 리모트 메시지를 처리

        val pm =
            getSystemService(Context.POWER_SERVICE) as PowerManager

        @SuppressLint("InvalidWakeLockTag")
        val wakeLock =
            pm.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "TAG",
            )
        wakeLock.acquire(3000)
        wakeLock.release()
        // Wake up screen
        // Wake up screen

        Log.d("MyFcmService", "Notification Title :: ${remoteMessage.notification?.title}")
        Log.d("MyFcmService", "Notification Body :: ${remoteMessage.notification?.body}")
        Log.d("MyFcmService", "Notification Data :: ${remoteMessage.data}")
        /*
        Notification Title :: CPR Angel의 출동이 필요합니다.
        Notification Body :: 경기도 화성시 송동 산49-57
2023-05-22 20:46:41.696  6977-7350  MyFcmService            com.example.cpr2u_android            D  Notification Data :: {call=67, type=1}
2023-05-22 20:46:41.696  6977-7350  FCM LOG                 com.example.cpr2u_android            D  From: 260439878640
2023-05-22 20:46:41.696  6977-7350  FCM LOG TYPE            com.example.cpr2u_android            D  Message data payload: 1
2023-05-22 20:46:41.697  6977-7350  FirebaseService         com.example.cpr2u_android            D  fcm >> 2
2023-05-22 20:46:41.697  6977-7350  JSON OBJECT             com.example.cpr2u_android            E  {"call":"67","type":"1"}
2023-05-22 20:46:41.697  6977-7350  FirebaseService         com.example.cpr2u_android            D  JSON OBJECT Type -> 1
         */

        Log.d("FCM LOG", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM LOG TYPE", "Message data payload: ${remoteMessage.data["type"]}")
            remoteMessage.data?.let {
                Timber.d("data 전달 받음 ")
                showNotification()
            }
        }

        val params: Map<String, String> = remoteMessage.data
        Timber.d("fcm >> ${remoteMessage.priority}")
        val jsonObject = JSONObject(params)
        Timber.tag("JSON OBJECT").e(jsonObject.toString())

        // E/JSON OBJECT: {"call":"228","type":"1"}
        val type = jsonObject.getString("type")
        if (type == "1") {
            // call 변수 안에 있는 call id를 바로 띄워야함
//            val intent = Intent(this, MainActivity::class.java)
//            val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        Timber.d("JSON OBJECT Type -> $type")
        jsonType = type
    }

    private fun showNotification() {
        /*
        1. 알림(노티피케이션)을 누를 시 이동할 화면을 정하여 Intent 객체를 생성한다.
        2. 이 인텐트는 당장 실행되는 것이 아니라 지연되므로 PendingIntent를 생성하여 위 Intent를 담는다.
        3. NotificationCompat.Builder를 사용할 채널 id 값을 전달하여 생성한다.
        4. 우선순위와 아이콘, 제목, 내용, 그리고 PendingIntent를 전달한다.
        5. NotificationManager 객체를 얻고, Android 8.0 이상인 경우 채널을 생성하도록 하고 알림을 띄운다.

        데이터를 백그라운드, 포그라운드에서 모두 접근하여 활용하려면 서버측에서 전송 시 노티피케이션 부분을 제거하고 데이터만 포함하도록 해야한다.
         */

        val intent = Intent(this, MainActivity::class.java)
//        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "channelId"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("환자 발생")
            .setContentText(jsonType)
//            .setContentIntent(pIntent)

        getSystemService(NotificationManager::class.java).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                createNotificationChannel(channel)
            }

            notify(Date().time.toInt(), notificationBuilder.build())
        }
    }
}

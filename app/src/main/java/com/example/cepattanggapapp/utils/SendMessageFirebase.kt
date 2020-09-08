package com.example.cepattanggapapp.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class SendMessageFirebase {

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val FCM_KEY = "AAAAU3lW6Zw:APA91bFd0qgzUvX7oZ3eaojFeN0LYYS0ZweS_BBq2uh5IRZsI_HzbmmZdcmKTvBom5VgXIkfRQ6Ym633_iWZVUnXgewZchK0tPGR2psAG9ZLqJmMrv2SmcCR-ACjEIPECgXRpvtmLPJu"
    private val serverKey = "key=$FCM_KEY"
    private val contentType = "application/json"

    fun sendMessage(message: String) {
        val topic = "/topics/sendMessage"
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
        if(message.isEmpty()) {
            val notification = JSONObject()
            val notificationBody = JSONObject()

            try {
                notificationBody.put("title", "Pesan Darurat")
                notificationBody.put("message", message)
                notification.put("to", topic)
                notification.put("data", notificationBody)
                Log.e("TAG","try")
            } catch (e: Exception) {
                Log.e("TAG", "onCreate: " + e.message)
            }
            sendNotification(notification)
        }

    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
    }
}
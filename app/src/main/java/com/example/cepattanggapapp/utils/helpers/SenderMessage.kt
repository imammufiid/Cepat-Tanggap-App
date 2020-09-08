package com.example.cepattanggapapp.utils.helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cepattanggapapp.fragment.BottomSheetFragment
import com.example.cepattanggapapp.utils.Constants
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class SenderMessage(context: Context?) {
    // messaging
    private var mCtx = context
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=${Constants.FCM_KEY}"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }
    private val topic = "/topics/sendMessage"
    private val user = Constants.getNamaUser(mCtx)


    fun senderMessage(message: String?) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
        Log.d("TOKEN_FCM", Constants.getTokenFCM(mCtx))
        if (message!!.isNotEmpty()) {
            val notification = JSONObject()
            val notificationBody = JSONObject()

            try {
                notificationBody.put("title", "Pesan Darurat dari $user")
                notificationBody.put("message", message)
                notification.put("to", topic)
//                notification.put("to", Constants.getTokenFCM(activity))
                notification.put("data", notificationBody)
                Log.e("TAG", "try")
            } catch (e: Exception) {
                Log.e("TAG", "onCreate: " + e.message)
            }
            sendNotification(notification)
        }
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest =
            object : JsonObjectRequest(FCM_API, notification, Response.Listener<JSONObject> {
                Log.i("TAG", "onResponse: $it")
            }, Response.ErrorListener {
                Toast.makeText(mCtx, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Authorization"] = serverKey
                    params["Content-Type"] = contentType
                    return params
                }
            }
        requestQueue.add(jsonObjectRequest)

    }
}
package com.example.cepattanggapapp.utils.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.cepattanggapapp.R
import com.example.cepattanggapapp.activities.MainActivity
import com.example.cepattanggapapp.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

@Suppress("DEPRECATION")
class MyFirebaseMessagingServices : FirebaseMessagingService() {
    private val ADMIN_CHANNEL_ID = "admin_channel"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
//        val token = FirebaseInstanceId.getInstance().token
//        Log.d("TAG", "Refreshed token: $token")
//
        Constants.setTokenFCM(this, p0!!)
//        if (user != null) {
//            //storeToken(refreshedToken!!)
//            Log.d("TOKEN", refreshedToken)
//        }
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        val intent = Intent(this, MainActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        // TODO(developer): Handle FCM messages here.
        Log.d("TAG", "From: " + p0?.from);

        // Check if message contains a data payload.
        if (p0?.data!!.isNotEmpty()) {
            Log.d("TAG", "Message data payload: " + p0.data);
        }

        // Check if message contains a notification payload.
        if (p0.notification != null) {
            Log.d("TAG", "Message Notification Body: " + p0.notification!!.body);
        }

        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them.
      */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_delete
        )

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_problem)
            .setLargeIcon(largeIcon)
            .setContentTitle(p0.data?.get("title"))
            .setContentText(p0.data?.get("message"))
            .setAutoCancel(true)
            .setSound(notificationSoundUri)
            .setContentIntent(pendingIntent)

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = resources.getColor(R.color.colorGreyDark)
        }
        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to devie notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }


}

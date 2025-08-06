package com.hexagraph.cropchain.data.remote.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hexagraph.cropchain.MainActivity
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MyFirebaseMessagingService() :
    FirebaseMessagingService() {
    @Inject
    lateinit var appPreferences: AppPreferences

    @ApplicationContext
    lateinit var context: Context

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Firebase Messaging Service", "Refreshed token: $token")

        CoroutineScope(Dispatchers.Default).launch {
            appPreferences.token.set(token)
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        Log.d("Firebase Messaging Service", "Message data payload: $data")
        val imageId = data["imageId"] ?: return
        val imageType = data["imageType"] ?: return
        val title = data["title"] ?: "New Notification"
        val body = data["body"] ?: ""


        sendNotification(this, imageId, imageType, title, body)
    }


    private fun sendNotification(context: Context, imageId: String, imageType: String, title: String, body: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("imageId", imageId)
            putExtra("imageType", imageType)
            putExtra("type","CropChain-FCM")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "upload_channel") // ✅ Use your existing channel
            .setSmallIcon(R.drawable.leaf) // Replace with your actual icon
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(imageId.toInt(), builder.build())
    }


}



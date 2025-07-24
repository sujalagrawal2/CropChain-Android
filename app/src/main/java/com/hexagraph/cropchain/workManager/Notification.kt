package com.hexagraph.cropchain.workManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hexagraph.cropchain.R

fun makeStatusNotification(message: String, context: Context, done: Int, total: Int) {

    // Make a channel if necessary
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "Practice Channel 2"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(2.toString(), name, importance)

        // Add the channel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification
    val builder = NotificationCompat.Builder(context, "2")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Uploading Image")
        .setContentText(message)
        .setProgress(total, done, false)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setOngoing(true)

    // Show the notification
    NotificationManagerCompat.from(context).notify(2, builder.build())
}

fun createForegroundNotification(context: Context, message: String, done: Int, total: Int): Notification {
    return NotificationCompat.Builder(context, "2")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Uploading Images")
        .setContentText(message)
        .setProgress(total, done, false)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(true)
        .build()
}



fun createForegroundNotification2(
    context: Context,
    contentText: String,
    progress: Int,
    total: Int
): Notification {
    return NotificationCompat.Builder(context, "upload_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Uploading to Pinata")
        .setContentText(contentText)
        .setProgress(total, progress, false)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .build()
}


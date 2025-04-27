package com.ucb.ucbtest.service
import android.Manifest
import android.R
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Util {
    companion object {

        fun sendNotificatión(context: Context) {
            // Check if the app has the POST_NOTIFICATIONS permission
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, so show the notification
                showNotification(context = context)
            } else {
                requestPermission(context)
            }
        }

        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        private fun showNotification(context: Context) {
            // Create NotificationChannel for Android 8.0+ (API 26+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "default_channel",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "This is a default notification channel."
                }

                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }

            // Create the notification
            val notification = NotificationCompat.Builder(context, "default_channel")
                .setContentTitle("New Notification")
                .setContentText("This is a simple notification with an icon.")
                .setSmallIcon(R.drawable.ic_dialog_info) // Set your icon here
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Automatically dismiss after user taps
                .build()

            // Show the notification
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, notification)
        }

        private fun requestPermission(context: Context) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }
}
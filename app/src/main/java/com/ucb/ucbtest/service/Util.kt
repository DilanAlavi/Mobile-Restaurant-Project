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
        private const val CHANNEL_ID = "cart_channel"
        private const val CHANNEL_NAME = "Notificaciones del Carrito"
        private const val CHANNEL_DESC = "Notificaciones cuando se agregan productos al carrito"

        fun sendNotificacionCarrito(context: Context, titulo: String, contenido: String) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
            ) {
                // Permiso ya otorgado o no es necesario
                showCartNotification(context, titulo, contenido)
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
        @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        private fun showCartNotification(context: Context, titulo: String, contenido: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = CHANNEL_DESC
                }
                val manager = context.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setSmallIcon(R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(context).notify(1001, notification)
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
package com.android.dailymood.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.dailymood.R

class ReminderReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, "daily_reminder_channel")
            .setSmallIcon(R.drawable.ic_profile)
            .setContentTitle("Como está seu humor hoje?")
            .setContentText("Não esqueça de registrar seu humor no DailyMood!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }
}

package com.mooncloak.vpn.util.notification

import androidx.core.app.NotificationCompat

internal fun NotificationCategory.toAndroidCategory(): String =
    when (this) {
        NotificationCategory.Call -> NotificationCompat.CATEGORY_CALL
        NotificationCategory.Navigation -> NotificationCompat.CATEGORY_NAVIGATION
        NotificationCategory.Message -> NotificationCompat.CATEGORY_MESSAGE
        NotificationCategory.Email -> NotificationCompat.CATEGORY_EMAIL
        NotificationCategory.Event -> NotificationCompat.CATEGORY_EVENT
        NotificationCategory.Promo -> NotificationCompat.CATEGORY_PROMO
        NotificationCategory.Alarm -> NotificationCompat.CATEGORY_ALARM
        NotificationCategory.Progress -> NotificationCompat.CATEGORY_PROGRESS
        NotificationCategory.Social -> NotificationCompat.CATEGORY_SOCIAL
        NotificationCategory.Error -> NotificationCompat.CATEGORY_ERROR
        NotificationCategory.Transport -> NotificationCompat.CATEGORY_TRANSPORT
        NotificationCategory.System -> NotificationCompat.CATEGORY_SYSTEM
        NotificationCategory.Service -> NotificationCompat.CATEGORY_SERVICE
        NotificationCategory.Reminder -> NotificationCompat.CATEGORY_REMINDER
        NotificationCategory.Recommendation -> NotificationCompat.CATEGORY_RECOMMENDATION
        NotificationCategory.Status -> NotificationCompat.CATEGORY_STATUS
        NotificationCategory.Workout -> NotificationCompat.CATEGORY_WORKOUT
        NotificationCategory.LocationSharing -> NotificationCompat.CATEGORY_LOCATION_SHARING
        NotificationCategory.StopWatch -> NotificationCompat.CATEGORY_STOPWATCH
        NotificationCategory.MissedCall -> NotificationCompat.CATEGORY_MISSED_CALL
        NotificationCategory.Voicemail -> NotificationCompat.CATEGORY_VOICEMAIL
    }

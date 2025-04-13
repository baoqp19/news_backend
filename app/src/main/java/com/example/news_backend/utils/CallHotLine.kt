package com.example.news_backend.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

object CallHotLine {
    fun call(activity: Activity) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", Constants.HOT_LINE, null))
        activity.startActivity(intent)
    }

    fun call(context: Context) {
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", Constants.HOT_LINE, null))
        context.startActivity(intent)
    }
}
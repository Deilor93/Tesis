package com.example.tesis.vista.util

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.view.PixelCopy
import android.view.View
import android.view.Window

fun capturaPantalla(activity: Activity, onScreenshotTaken: (Bitmap?) -> Unit) {
    val window: Window = activity.window
    val view: View = window.decorView.rootView

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        PixelCopy.request(window, bitmap, { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
                onScreenshotTaken(bitmap)
            } else {
                onScreenshotTaken(null)
            }
        }, view.handler)
    } else {
        //Para versiones viejas, ustiliza el cache
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        onScreenshotTaken(bitmap)
    }
}

package com.spdigital.weatherapp.workers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView


class DownloadImageWorker(internal var bmImage: ImageView) :
    AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg urls: String): Bitmap? {
        val url = urls[0]
        var bmp: Bitmap? = null
        try {
            val inputStream = java.net.URL(url).openStream()
            bmp = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bmp
    }
    override fun onPostExecute(result: Bitmap) {
        bmImage.setImageBitmap(result)
    }
}
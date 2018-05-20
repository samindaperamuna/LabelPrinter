package com.fifthgen.labelprinter.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

class InternetCheck(context: Context) : AsyncTask<Unit, Unit, Unit>() {

    private var listener: InternetCheckListener? = null
    private val contextReference = WeakReference<Context>(context)

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = contextReference.get()?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

    override fun doInBackground(vararg params: Unit) {
        val b = hasInternetAccess()

        Handler(Looper.getMainLooper()).post({ listener!!.onComplete(b) })
    }

    fun isInternetConnectionAvailable(x: InternetCheckListener) {
        listener = x
        execute()
    }

    private fun hasInternetAccess(): Boolean {
        if (isNetworkAvailable) {
            try {
                val url = URL("http://clients3.google.com/generate_204").openConnection() as HttpURLConnection
                url.setRequestProperty("User-Agent", "Android")
                url.setRequestProperty("Connection", "close")
                url.connectTimeout = 1500
                url.connect()
                return url.responseCode == 204 && url.contentLength == 0
            } catch (e: IOException) {
                Log.e(this::class.qualifiedName, "An error occurred while connecting to the internet!")
            }
        } else {
            Log.d(this::class.qualifiedName, "No network available!")
        }

        return false
    }

    interface InternetCheckListener {
        fun onComplete(connected: Boolean)
    }
}
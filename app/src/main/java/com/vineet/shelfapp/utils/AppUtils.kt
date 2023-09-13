package com.vineet.shelfapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.vineet.shelfapp.R
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

//    to check whether the network is available or not
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    //to set round edges image using Glide
    fun setRoundedImage(
        imageUrl: String?,
        context: Context,
        appCompatImageView: ImageView,
        topLeft: Float,
        topRight: Float,
        bottomLeft: Float,
        bottomRight: Float,

        ) {
        try {
            Glide.with(context).load(imageUrl)
                .transform(GranularRoundedCorners(topLeft, topRight, bottomLeft, bottomRight))
                .signature(ObjectKey(imageUrl ?: ""))
                .into(appCompatImageView)
        } catch (e: Exception) {

        }
    }

    //to convert long to date as per format
    fun convertLongToDate(time: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getDefault()
        val date = Date(time)
        return dateFormat.format(date)
    }

    // utility to check whether the enter password is valid or not
    fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
        return if (password.length < 8 || password.length > 265) false else {
            password.matches(Regex(passwordPattern))
        }
    }

    //to set circular image using Glide
    fun setCircularImage(
        imageUrl: String?,
        imageView: ImageView

    ) {

        Glide.with(imageView.context)
            .load(imageUrl)
            .circleCrop()
            .into(imageView)
    }
}
package org.devjj.platform.nurbanhoney.core.platform

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.kakao.sdk.network.origin
import dagger.hilt.android.qualifiers.ApplicationContext
import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.exception.Failure.ServerError
import org.devjj.platform.nurbanhoney.core.exception.Failure.TokenError
import org.devjj.platform.nurbanhoney.core.extension.connectivityManager
import org.devjj.platform.nurbanhoney.core.functional.Either
import org.devjj.platform.nurbanhoney.core.functional.Either.Left
import org.devjj.platform.nurbanhoney.core.functional.Either.Right
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject constructor(@ApplicationContext private val context: Context) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.connectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun <T, R> request(
        call: Call<T>,
        transform: (T) -> R,
        default: T
    ): Either<Failure, R> {
        return try {
            val response = call.execute()
            Log.d("error_check__", "before")
            when (response.isSuccessful) {
                true -> Right(transform((response.body() ?: default)))
                false -> {
                    Log.d("error_kind_check__", "${response.code()}")
                    if (response.code() == 401) {
                        Left(TokenError)
                    }else {
                        Left(ServerError)
                    }

                }
            }
        } catch (exception: Throwable) {
            exception.stackTrace.forEach {
                Log.d("error_msg_check__", "${it}")
            }
            Log.d("error_msg_check__", "${exception.localizedMessage}")
            Log.d("error_msg_check__", "${exception.suppressed}")
            Log.d("error_msg_check__", "${exception.cause}")
            Log.d("error_msg_check__", "${exception.message}")
            Log.d("error_msg_check__", "${exception.origin}")
            Left(ServerError)
        }
    }
}
package org.devjj.platform.nurbanhoney.features.network

import org.devjj.platform.nurbanhoney.core.exception.Failure
import org.devjj.platform.nurbanhoney.core.functional.Either
import retrofit2.Call

fun <T, R> request(
    call: Call<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> {
    return try {
        val response = call.execute()
        //Log.d("error_check__", "before")
        when (response.isSuccessful) {
            true -> Either.Right(transform((response.body() ?: default)))
            false -> {
//                        Log.d("error_check__kind_", "${response.code()}")
//                        Log.d("error_check__kind_", "${response.raw().message}")
                if (response.code() == 401) {
                    Either.Left(Failure.TokenError)
                }else {
                    Either.Left(Failure.ServerError)
                }

            }
        }
    } catch (exception: Throwable) {
        exception.stackTrace.forEach {
//                    Log.d("error_msg_check__", "${it}")
        }
//                Log.d("error_msg_check__", "${exception.localizedMessage}")
//                Log.d("error_msg_check__", "${exception.suppressed}")
//                Log.d("error_msg_check__", "${exception.cause}")
//                Log.d("error_msg_check__", "${exception.message}")
//                Log.d("error_msg_check__", "${exception.origin}")
        Either.Left(Failure.ServerError)
    }
}
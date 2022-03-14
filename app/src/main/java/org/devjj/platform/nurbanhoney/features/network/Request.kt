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
        when (response.isSuccessful) {
            true -> Either.Right(transform((response.body() ?: default)))
            false -> {
                if (response.code() == 401) {
                    Either.Left(Failure.TokenError)
                }else {
                    Either.Left(Failure.ServerError)
                }

            }
        }
    } catch (exception: Throwable) {
        Either.Left(Failure.ServerError)
    }
}
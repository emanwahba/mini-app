package com.mobiquity.miniapp.model.repository

import com.mobiquity.miniapp.utils.Result
import retrofit2.Response
import java.lang.Exception

abstract class BaseRepository {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Result.Success(body)
                }
            }
            return Result.Error("${response.code()}" + " " + response.message())
        } catch (e: Exception) {
            return Result.Error(e.message ?: e.toString())
        }
    }
}
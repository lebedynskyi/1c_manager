package com.simple.games.tradeassist.data.api

import com.simple.games.tradeassist.domain.AppException
import com.simple.games.tradeassist.domain.CoroutineAware
import com.simple.games.tradeassist.domain.NetworkStateDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject

abstract class RetrofitApiDataSource(
    coroutineDispatcher: CoroutineDispatcher,
) : CoroutineAware(coroutineDispatcher) {
    @Inject
    lateinit var networkState: NetworkStateDataSource

    private val errorMapper = ApiErrorMapper()

    suspend fun <T> apiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            if (!networkState.isNetworkConnected) {
                // Workaround for UI for case of switching bottom tabs.
                delay(400)
                Result.failure(AppException.NoInternet)
            } else {
                val response = call.invoke()
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    errorMapper.mapApiError(response).let { error ->
                        Result.failure(error)
                    }
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            errorMapper.mapNetworkError(t).let { error ->
                Result.failure(error)
            }
        }
    }

    class ApiErrorMapper {
        private val jsonParser = Json { ignoreUnknownKeys = true }

        fun mapApiError(response: Response<*>): AppException {
            return try {
                val errorData = response.errorBody()?.byteStream()?.bufferedReader()?.readText() ?: "{}"
                System.err.println(errorData)
//                val errorModel: ResponseApiError = jsonParser.decodeFromString(errorData)
//                mapApiError(errorModel)
                AppException.Unknown
            } catch (e: Exception) {
                mapNetworkError(e)
            }
        }

        fun mapNetworkError(throwable: Throwable): AppException {
            return AppException.Unknown
        }

        private fun mapApiError(): AppException {
            return AppException.Unknown
        }
    }
}
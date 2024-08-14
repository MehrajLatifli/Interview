package com.example.interview.source.api.repositories.profile

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository  @Inject constructor(private val api: IApiManager) {



    suspend fun getprofile()=safeApiRequest{

            api.getprofile()

    }

    suspend fun logout()=safeApiRequest {
            api.logout()

    }



    private suspend fun <T> safeApiRequest(request: suspend () -> Response<T>) = flow<Resource<T>> {
        try {
            val response = request.invoke()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = "Message: ${errorResponse.title}" ?: "Unknown error"
                Log.e("Response error", errorMessage)
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("APIFailed", e.localizedMessage ?: "Unknown error")
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.catch {
        Log.e("APIFailed", it.localizedMessage ?: "Unknown error")
        emit(Resource.Error(it.localizedMessage ?: "Unknown error"))
    }.flowOn(Dispatchers.IO)

}

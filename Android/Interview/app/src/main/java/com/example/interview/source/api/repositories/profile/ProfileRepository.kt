package com.example.interview.source.api.repositories.profile

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.models.responses.post.login.Login
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.models.responses.post.token.RefreshTokenRequest
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository  @Inject constructor(private val api: IApiManager) {



    suspend fun getprofile()=safeApiRequest{

            api.getprofile()

    }

    suspend fun logout()=safeApiRequest {
            api.logout()

    }




    suspend fun <T> safeApiRequest(request: suspend () -> Response<T>) = flow<Resource<T>> {
        try {
            val response = request.invoke()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Log.e("Response error", errorBody)
                emit(Resource.Error(errorBody))
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

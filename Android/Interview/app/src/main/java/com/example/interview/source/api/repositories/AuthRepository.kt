package com.example.interview.source.api.repositories

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.example.interview.models.responses.post.registration.Register
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: IApiManager) {

    suspend fun registerUser(register: Register) = safeApiRequest {
        val usernamePart = register.username?.toRequestBody("multipart/form-data".toMediaType())
            ?: "".toRequestBody("multipart/form-data".toMediaType())
        val emailPart = register.email?.toRequestBody("multipart/form-data".toMediaType())
            ?: "".toRequestBody("multipart/form-data".toMediaType())
        val passwordPart = register.password?.toRequestBody("multipart/form-data".toMediaType())
            ?: "".toRequestBody("multipart/form-data".toMediaType())
        val phoneNumberPart = register.phoneNumber?.toRequestBody("multipart/form-data".toMediaType())
            ?: "".toRequestBody("multipart/form-data".toMediaType())

        val imagePathPart = register.imagePath?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("imagePath", it.name, requestFile)
        }

        api.createAccount(
            username = usernamePart,
            email = emailPart,
            password = passwordPart,
            phoneNumber = phoneNumberPart,
            imagePath = imagePathPart
        )
    }

    private suspend fun <T> safeApiRequest(request: suspend () -> Response<T>) = flow<Resource<Unit>> {
        try {
            val response = request()
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                val errorResponse = parseErrorResponse(response)
                emit(Resource.Error(errorResponse))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Request failed: ${e.localizedMessage}")
            emit(Resource.Error(ErrorResponse(
                date = "Unknown",
                machine = "Unknown",
                status = -1,
                title = e.localizedMessage ?: "Unknown error",
                user = "Unknown"
            )))
        }
    }.catch {
        Log.e("AuthRepository", "Flow failed: ${it.localizedMessage}")
        emit(Resource.Error(ErrorResponse(
            date = "Unknown",
            machine = "Unknown",
            status = -1,
            title = it.localizedMessage ?: "Unknown error",
            user = "Unknown"
        )))
    }.flowOn(Dispatchers.IO)

    private fun <T> parseErrorResponse(response: Response<T>): ErrorResponse? {
        return try {
            val errorBody = response.errorBody()?.string() ?: return null
            Gson().fromJson(errorBody, ErrorResponse::class.java)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error parsing error response: ${e.localizedMessage}")
            null
        }
    }
}

package com.example.interview.source.api.repositories.auth

import android.util.Log
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.models.responses.post.login.Login
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: IApiManager) {

    suspend fun registerAdmin(register: Register) = safeApiRequest {
        val usernamePart = register.username?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val emailPart = register.email?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val passwordPart = register.password?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val phoneNumberPart = register.phoneNumber?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())

        val imagePathPart = register.imagePath?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("imagePath", it.name, requestFile)
        }

        api.registerAdmin(
            username = usernamePart,
            email = emailPart,
            password = passwordPart,
            phoneNumber = phoneNumberPart,
            imagePath = imagePathPart
        )
    }

    suspend fun registerHR(register: Register) = safeApiRequest {
        val usernamePart = register.username?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val emailPart = register.email?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val passwordPart = register.password?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val phoneNumberPart = register.phoneNumber?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())

        val imagePathPart = register.imagePath?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("imagePath", it.name, requestFile)
        }

        api.registerHR(
            username = usernamePart,
            email = emailPart,
            password = passwordPart,
            phoneNumber = phoneNumberPart,
            imagePath = imagePathPart
        )
    }

    suspend fun login(username: String, password: String): Resource<LoginResponse> {
        return safeApiRequest {
            api.login(Login(username, password))
        }
    }



    suspend fun <T> safeApiRequest(request: suspend () -> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = request.invoke()

                if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)?.title
                        ?: response.message()
                    Log.e("Resource.Error", errorMessage ?: "Unknown error")
                    Resource.Error(errorMessage ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("Resource.Error", e.localizedMessage ?: "Unknown error")
                Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}

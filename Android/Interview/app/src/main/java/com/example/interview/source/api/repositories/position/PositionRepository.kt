package com.example.interview.source.api.repositories.position

import android.util.Log
import com.example.interview.models.responses.get.candidate.CandidateDocumentResponse
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.position.PositionResponse
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class PositionRepository @Inject constructor(private val api: IApiManager) {

    suspend fun getAllPositions() =safeApiRequest{

        api.getAllPositions()

    }

    suspend fun getPositionByID(id: Int): Resource<PositionResponse> {
        return safeApiRequest {
            api.getPositionByID(id)
        }
    }


    private suspend fun <T> safeApiRequest(request: suspend () -> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = request.invoke()
                if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)?.title
                        ?: response.message()
                    Log.e("Resource.Error", "Error response: $errorMessage")
                    Resource.Error(errorMessage ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("Resource.Error", "Exception: ${e.localizedMessage}")
                Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
package com.example.interview.source.api.repositories.candidate

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.post.candidate.Candidate
import com.example.interview.models.responses.post.candidatedocument.CandidateDocument
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

class CandidateRepository @Inject constructor(private val api: IApiManager)  {

    suspend fun registerCandidatedocument(candidatedocument: CandidateDocument) = safeApiRequest {
        val surnamePart = candidatedocument.surname?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val namePart = candidatedocument.name?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val patronymicPart = candidatedocument.patronymic?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val phonenumberPart = candidatedocument.phonenumber?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())
        val emailPart = candidatedocument.email?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())

        val cvPart = candidatedocument.cv?.let {
            val requestFile = it.asRequestBody("application/*".toMediaType())
            MultipartBody.Part.createFormData("cv", it.name, requestFile)
        }

        val addressPart = candidatedocument.address?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())

        api.registerCandidatedocument(
            surname = surnamePart,
            name = namePart,
            patronymic = patronymicPart,
            phonenumber = phonenumberPart,
            email = emailPart,
            cv = cvPart,
            address = addressPart
        )
    }

    suspend fun registerCandidate(candidate: Candidate) = safeApiRequest {

        val candidateDocumentIdRequestBody = candidate.candidateDocumentId
            ?.toString()
            ?.toRequestBody("text/plain".toMediaType())
            ?: "".toRequestBody("text/plain".toMediaType())

        api.registerCandidate(candidateDocumentIdRequestBody)
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
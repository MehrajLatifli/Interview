package com.example.interview.source.api.repositories.candidate
import android.util.Log
import com.example.interview.models.responses.get.candidatedocument.CandidateDocumentResponse
import com.example.interview.models.responses.get.candidate.CandidateResponse
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.post.candidate.CandidateRequest
import com.example.interview.models.responses.post.candidatedocument.CandidateDocumentRequest
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

class CandidateRepository @Inject constructor(private val api: IApiManager) {

    suspend fun registerCandidatedocument(candidateDocumentRequest: CandidateDocumentRequest): Resource<Unit> {
        return safeApiRequest {
            val surnamePart = candidateDocumentRequest.surname?.toRequestBody("text/plain".toMediaType())
                ?: "".toRequestBody("text/plain".toMediaType())
            val namePart = candidateDocumentRequest.name?.toRequestBody("text/plain".toMediaType())
                ?: "".toRequestBody("text/plain".toMediaType())
            val patronymicPart = candidateDocumentRequest.patronymic?.toRequestBody("text/plain".toMediaType())
                ?: "".toRequestBody("text/plain".toMediaType())
            val phoneNumberPart = candidateDocumentRequest.phonenumber?.toRequestBody("text/plain".toMediaType())
                ?: "".toRequestBody("text/plain".toMediaType())
            val emailPart = candidateDocumentRequest.email?.toRequestBody("text/plain".toMediaType())
                ?: "".toRequestBody("text/plain".toMediaType())

            val cvPart = candidateDocumentRequest.cv?.let {
                val requestFile = it.asRequestBody("application/*".toMediaType())
                MultipartBody.Part.createFormData("cv", it.name, requestFile)
            }

            val addressPart = candidateDocumentRequest.address?.toRequestBody("text/plain".toMediaType())
                ?: "".toRequestBody("text/plain".toMediaType())

            api.registerCandidatedocument(
                surname = surnamePart,
                name = namePart,
                patronymic = patronymicPart,
                phoneNumber = phoneNumberPart,
                email = emailPart,
                cv = cvPart,
                address = addressPart
            )
        }
    }

    suspend fun registerCandidate(candidateDocumentId: Int): Resource<CandidateResponse> {
        return safeApiRequest {
            api.registerCandidate(CandidateRequest(candidateDocumentId))
        }
    }
    suspend fun getAllCandidateDocuments() =safeApiRequest{

        api.getAllCandidateDocuments()

    }

    suspend fun getAllCandidates() =safeApiRequest{

        api.getAllCandidates()

    }

    suspend fun getCandidateDocumentByID(id: Int): Resource<CandidateDocumentResponse> {
        return safeApiRequest {
            api.getCandidateDocumentByID(id)
        }
    }

    suspend fun deleteCandidateDocumentById(id:Int) =safeApiRequest{

        api.deleteCandidateDocumentById(id)

    }

    suspend fun updateCandidateDocument(id: Int, candidateDocumentRequest: CandidateDocumentRequest): Resource<Unit> {
        return safeApiRequest {

            val surnamePart = candidateDocumentRequest.surname?.toRequestBody("text/plain".toMediaType())
            val namePart = candidateDocumentRequest.name?.toRequestBody("text/plain".toMediaType())
            val patronymicPart = candidateDocumentRequest.patronymic?.toRequestBody("text/plain".toMediaType())
            val phoneNumberPart = candidateDocumentRequest.phonenumber?.toRequestBody("text/plain".toMediaType())
            val emailPart = candidateDocumentRequest.email?.toRequestBody("text/plain".toMediaType())

            val cvPart = candidateDocumentRequest.cv?.let {
                val requestFile = it.asRequestBody("application/*".toMediaType())
                MultipartBody.Part.createFormData("cv", it.name, requestFile)
            }

            val addressPart = candidateDocumentRequest.address?.toRequestBody("text/plain".toMediaType())

            api.updateCandidateDocument(
                id = id.toString().toRequestBody("text/plain".toMediaType()),
                surname = surnamePart,
                name = namePart,
                patronymic = patronymicPart,
                phoneNumber = phoneNumberPart,
                email = emailPart,
                cv = cvPart,
                address = addressPart
            )
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



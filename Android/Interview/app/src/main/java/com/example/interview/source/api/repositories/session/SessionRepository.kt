package com.example.interview.source.api.repositories.session

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.models.responses.post.session.SessionRequest
import com.example.interview.models.responses.post.vacancy.VacancyRequest
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class SessionRepository @Inject constructor(private val api: IApiManager) {

    // These functions are declared as suspend, which is correct
    suspend fun getAllSession() = safeApiRequest {
        api.getAllSession()
    }

    suspend fun getAllOwnSession() = safeApiRequest {
        api.getAllOwnSession()
    }

    suspend fun getSessionByID(id: Int) = safeApiRequest {
        api.getSessionByID(id)
    }





    suspend fun addSession(sessionRequest: SessionRequest) = safeApiRequest {
        api.addSession(sessionRequest)
    }



    suspend fun deleteSessionById(id: Int) = safeApiRequest {
        api.deleteSessionById(id)
    }

    suspend fun updateSession(sessionResponse: SessionResponse) = safeApiRequest {
        api.updateSession(sessionResponse)
    }

    private suspend fun <T> safeApiRequest(request: suspend () -> Response<T>) = flow<Resource<T>> {
        try {
            val response = request.invoke()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.title ?: "Unknown error"
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

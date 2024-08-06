package com.example.interview.source.api.repositories.sessionquestion

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.models.responses.get.sessionquestion.SessionQuestionResponse
import com.example.interview.models.responses.post.session.SessionRequest
import com.example.interview.models.responses.post.sessionquestion.RandomQuestionRequest2
import com.example.interview.models.responses.post.sessionquestion.SessionQuestionRequest
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class SessionQuestionRepository @Inject constructor(private val api: IApiManager) {

    suspend fun getAllSessionQuestion() = safeApiRequest {
        api.getAllSessionQuestion()
    }

    suspend fun getSessionQuestionByID(id: Int) = safeApiRequest {
        api.getSessionQuestionByID(id)


    }



    suspend fun getSessionQuestionBySessionId(sessionId: Int) = safeApiRequest {
        api.getSessionQuestionBySessionId(sessionId)
    }



    suspend fun getQuestionByID(id: Int) = safeApiRequest {
        api.getQuestionByID(id)
    }


    suspend fun addSessionQuestion(sessionQuestionRequest: SessionQuestionRequest) = safeApiRequest {
        api.addSessionQuestion(sessionQuestionRequest)
    }

    suspend fun deleteSessionQuestionById(id: Int) = safeApiRequest {
        api.deleteSessionQuestionById(id)
    }


    suspend fun updateSessionQuestion(sessionQuestionResponse: SessionQuestionResponse) = safeApiRequest {
        api.updateSessionQuestion(sessionQuestionResponse)
    }


    suspend fun addRandomSessionQuestion(questionCount: Int, vacantionId: Int, sessionId: Int) = safeApiRequest {
        api.addRandomSessionQuestion(RandomQuestionRequest2( questionCount, vacantionId, sessionId))
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
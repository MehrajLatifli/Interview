package com.example.interview.source.api.repositories.vacancy

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.position.PositionResponse
import com.example.interview.models.responses.get.structure.StructureResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.models.responses.post.vacancy.VacancyRequest
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

class VacancyRepository @Inject constructor(private val api: IApiManager) {




    fun getAllVacancies(): Flow<Resource<List<VacancyResponse>>> = safeApiRequest { api.getAllVacancies() }

    fun getVacancyByID(id: Int) = safeApiRequest { api.getVacancyByID(id) }

    fun addVacancy(vacancyRequest: VacancyRequest) = safeApiRequest {
        api.addVacancy(vacancyRequest)
    }


    fun deleteVacancyById(id: Int) = safeApiRequest { api.deleteVacancyById(id) }


    fun updateVacancy(vacancy: VacancyResponse) = safeApiRequest {
        api.updateVacancy(vacancy)
    }


    private fun <T> safeApiRequest(request: suspend () -> Response<T>) = flow<Resource<T>> {
        try {
            val response = request()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse?.title ?: response.message()
                Log.e("Resource.Error", "Error response: $errorMessage")
                emit(Resource.Error(errorMessage ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("Resource.Error", "Exception: ${e.localizedMessage}")
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.catch { e ->
        Log.e("Resource.Error", "Flow exception: ${e.localizedMessage}")
        emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }.flowOn(Dispatchers.IO)
}

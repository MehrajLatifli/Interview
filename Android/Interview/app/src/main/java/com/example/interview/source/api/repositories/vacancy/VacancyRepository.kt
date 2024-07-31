package com.example.interview.source.api.repositories.vacancy

import android.util.Log
import com.example.interview.models.responses.get.error.ErrorResponse
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.structure.StructureResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.models.responses.post.login.Login
import com.example.interview.models.responses.post.vacancy.Vacancy
import com.example.interview.source.api.IApiManager
import com.example.interview.source.api.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class VacancyRepository @Inject constructor(private val api: IApiManager) {


    suspend fun getAllVacancies() =safeApiRequest{

        api.getAllVacancies()

    }

    suspend fun getVacancyByID(id: Int): Resource<VacancyResponse> {
        return safeApiRequest {
            api.getVacancyByID(id)
        }
    }

    suspend fun addVacancy(vacancy: Vacancy): Resource<Unit> {
        return safeApiRequest {
            api.addVacancy(Vacancy(vacancy.title, vacancy.description, vacancy.startDate, vacancy.endDate,vacancy.positionId,vacancy.structureId))
        }
    }

    suspend fun deleteVacancyById(id:Int) =safeApiRequest{

        api.deleteVacancyById(id)

    }

    suspend fun updateVacancy(id:Int, vacancy: Vacancy) =safeApiRequest{

        api.updateVacancy(id,Vacancy(vacancy.title, vacancy.description, vacancy.startDate, vacancy.endDate,vacancy.positionId,vacancy.structureId) )

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
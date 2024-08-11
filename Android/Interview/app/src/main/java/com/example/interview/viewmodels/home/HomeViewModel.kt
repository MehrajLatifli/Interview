package com.example.interview.viewmodels.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.candidatedocument.CandidateDocumentResponse
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.candidate.CandidateRepository
import com.example.interview.source.api.repositories.position.PositionRepository
import com.example.interview.source.api.repositories.session.SessionRepository
import com.example.interview.source.api.repositories.structure.StructureRepository
import com.example.interview.source.api.repositories.vacancy.VacancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val vacancyRepository: VacancyRepository,
    private val candidateRepository: CandidateRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _vacancies = MutableLiveData<List<VacancyResponse>>()
    val vacancies: LiveData<List<VacancyResponse>> = _vacancies

    private val _vacancy = MutableLiveData<VacancyResponse>()
    val vacancy: LiveData<VacancyResponse> = _vacancy

    private val _candidateDocuments = MutableLiveData<List<CandidateDocumentResponse>>()
    val candidateDocuments: LiveData<List<CandidateDocumentResponse>> = _candidateDocuments

    private val _candidateDocument = MutableLiveData<CandidateDocumentResponse>()
    val candidateDocument: LiveData<CandidateDocumentResponse> = _candidateDocument

    private val _sessions = MutableLiveData<List<SessionResponse>>()
    val sessions: LiveData<List<SessionResponse>> = _sessions

    private val _session = MutableLiveData<SessionResponse>()
    val session: LiveData<SessionResponse> = _session


    fun getAllOwnSession(): List<SessionResponse> {
        _loading.value = true
        viewModelScope.launch {

            sessionRepository.getAllOwnSession().collect { result ->

                when (result) {
                    is Resource.Success -> {
                        _loading.postValue(false)
                        _sessions.postValue(result.data ?: emptyList())
                        Log.d("SessionViewModel", "Sessions: ${result.data}")
                    }

                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("SessionViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }

        return _sessions.value.orEmpty()
    }

    fun getSessionByID(id: Int) {
        _loading.value = true
        viewModelScope.launch {
            sessionRepository.getSessionByID(id).collect { result ->

                when (result) {
                    is Resource.Success -> {
                        _loading.postValue(false)
                        _session.postValue(result.data!!)
                        Log.d("SessionViewModel", "Sessions: ${result.data}")
                    }

                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("SessionViewModel", result.message ?: "Unknown error")
                    }
                }
            }

        }
    }

    fun getAllCandidateDocuments(): List<CandidateDocumentResponse> {
        _loading.value = true
        viewModelScope.launch {
            val result = candidateRepository.getAllCandidateDocuments()
            if (result is Resource.Success) {

                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _candidateDocuments.value = itemResponse!!
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("HomeViewModel", result.message ?: "Unknown error")
            }
        }

        return _candidateDocuments.value.orEmpty()
    }

    fun getCandidateDocumentByID(id:Int) {
        _loading.value = true
        viewModelScope.launch {
            val result = candidateRepository.getCandidateDocumentByID(id)
            if (result is Resource.Success) {

                delay(1000)
                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _candidateDocument.value = itemResponse!!
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("HomeViewModel", result.message ?: "Unknown error")
            }
        }

    }

    fun getAllVacancies() : List<VacancyResponse> {
        _loading.value = true

        viewModelScope.launch {
            vacancyRepository.getAllVacancies().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _loading.postValue(false)
                        _vacancies.postValue(result.data ?: emptyList())
                        Log.d("HomeViewModel", "Vacancies fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("HomeViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }

        return _vacancies.value.orEmpty()
    }

    fun getVacancyByID(id: Int) {
        _loading.value = true

        viewModelScope.launch {
            vacancyRepository.getVacancyByID(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(200) // Simulate network delay
                        _loading.postValue(false)
                        _vacancy.postValue(result.data!!)
                        Log.d("HomeViewModel", "Vacancy fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("HomeViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }
    }
}
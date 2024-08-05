package com.example.interview.viewmodels.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.candidate.CandidateDocumentResponse
import com.example.interview.models.responses.get.candidate.CandidateResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.models.responses.get.profile.Role
import com.example.interview.models.responses.get.profile.UserClaim
import com.example.interview.models.responses.get.session.SessionResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.candidate.CandidateRepository
import com.example.interview.source.api.repositories.position.PositionRepository
import com.example.interview.source.api.repositories.profile.ProfileRepository
import com.example.interview.source.api.repositories.session.SessionRepository
import com.example.interview.source.api.repositories.structure.StructureRepository
import com.example.interview.source.api.repositories.vacancy.VacancyRepository
import com.example.interview.utilities.Constants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val vacancyRepository: VacancyRepository,
    private val candidateRepository: CandidateRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _completeResult = MutableLiveData<Boolean>()
    val completeResult: LiveData<Boolean> = _completeResult

    private val _afterDeleteResult = MutableLiveData<Boolean>()
    val afterDeleteResult: LiveData<Boolean> = _afterDeleteResult

    private val _afterUpdateResult = MutableLiveData<Boolean>()
    val afterUpdateResult: LiveData<Boolean> = _afterUpdateResult

    private val _sessions = MutableLiveData<List<SessionResponse>>()
    val sessions: LiveData<List<SessionResponse>> = _sessions

    private val _session = MutableLiveData<SessionResponse>()
    val session: LiveData<SessionResponse> = _session

    private val _vacancies = MutableLiveData<List<VacancyResponse>>()
    val vacancies: LiveData<List<VacancyResponse>> = _vacancies

    private val _vacancy = MutableLiveData<VacancyResponse>()
    val vacancy: LiveData<VacancyResponse> = _vacancy

    private val _candidateDocuments = MutableLiveData<List<CandidateDocumentResponse>>()
    val candidatedocuments: LiveData<List<CandidateDocumentResponse>> = _candidateDocuments

    private val _candidateDocument = MutableLiveData<CandidateDocumentResponse>()
    val candidateDocument: LiveData<CandidateDocumentResponse> = _candidateDocument

    private val _profiles = MutableLiveData<List<ProfileResponse>>()
    val profiles: LiveData<List<ProfileResponse>> = _profiles

    private val _userclaims = MutableLiveData<List<UserClaim>>()
    val userclaims: LiveData<List<UserClaim>> = _userclaims

    private val _roles = MutableLiveData<List<Role>>()
    val roles: LiveData<List<Role>> = _roles

    fun getAllSession(): List<SessionResponse> {
        _loading.value = true
        viewModelScope.launch {

            sessionRepository.getAllSession().collect { result ->

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

    fun getVacancyByID(id:Int) {
        _loading.value = true
        viewModelScope.launch {
            vacancyRepository.getVacancyByID(id).collect { result ->
                when (result) {
                    is Resource.Success -> {

                        _loading.postValue(false)
                        val itemResponse = result.data
                        if (itemResponse != null) {
                            _vacancy.value = itemResponse!!
                            Log.d("SessionViewModel", "Vacancy: ${result.data}")
                        }

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

    fun getCandidateDocumentByID(id:Int) {
        _loading.value = true
        viewModelScope.launch {
            val result = candidateRepository.getCandidateDocumentByID(id)
            if (result is Resource.Success) {

                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _candidateDocument.value = itemResponse!!
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("SessionViewModel", result.message ?: "Unknown error")
            }
        }

    }


    fun getprofile() : List<ProfileResponse> {
        _loading.value = true

        viewModelScope.launch {

            profileRepository.getprofile().collectLatest { response: Resource<ProfileResponse> ->

                when (response) {
                    is Resource.Success -> {

                        _loading.value = false
                        val itemResponse = response.data
                        if (itemResponse != null) {
                            _profiles.value = listOf(itemResponse)
                            Log.e("Profile", _profiles.value.toString())

                            val permitions = itemResponse?.permitions
                            if (permitions != null && permitions.isNotEmpty()) {

                                permitions.forEach { item ->

                                    _userclaims.value = item.userClaims
                                    _roles.value = item.roles

                                }

                            } else {
                                _userclaims.value = emptyList()
                                _roles.value = emptyList()
                            }

                        } else {

                            _error.value = "No profile found"
                            _profiles.value = emptyList()
                            Log.e("APIFailed", _error.value.toString())
                        }
                    }
                    is Resource.Error -> {
                        _loading.value = false
                        _error.value = "Failed to fetch profile: ${response.message}"
                        Log.e("APIFailed", _error.value.toString())
                    }
                }
            }

        }

        return _profiles.value.orEmpty()
    }

    fun deleteSessionById(documentId: Int) {
        _loading.value = true

        viewModelScope.launch {
            sessionRepository.deleteSessionById(documentId).collect { result ->

                when (result) {
                    is Resource.Success -> {

                        _loading.postValue(false)
                        _afterDeleteResult.postValue(true)
                        Log.d("SessionViewModel", "Session deleted successfully")
                    }

                    is Resource.Error -> {
                        _afterDeleteResult.postValue(false)
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e(
                            "SessionViewModel",
                            "Failed to delete Session: ${result.message ?: "Unknown error"}"
                        )
                    }
                }
            }
        }
    }
}

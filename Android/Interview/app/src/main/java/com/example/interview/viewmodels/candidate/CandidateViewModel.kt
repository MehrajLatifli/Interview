package com.example.interview.viewmodels.candidate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.post.candidate.Candidate
import com.example.interview.models.responses.post.candidatedocument.CandidateDocument
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.candidate.CandidateRepository
import com.example.interview.source.local.repositories.EntityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel  @Inject constructor (private val candidateRepository: CandidateRepository) : ViewModel() {


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> = _authResult

    fun registerCandidate(candidate: Candidate) {
        _loading.value = true

        viewModelScope.launch {
            val result = candidateRepository.registerCandidate(candidate)
            if (result is Resource.Success) {
                _authResult.postValue(true)
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _authResult.postValue(false)
            }
        }
    }

    fun registerCandidatedocument(candidateDocument: CandidateDocument) {
        _loading.value = true

        viewModelScope.launch {
            val result = candidateRepository.registerCandidatedocument(candidateDocument)
            if (result is Resource.Success) {
                _authResult.postValue(true)
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _authResult.postValue(false)
            }
        }
    }
}
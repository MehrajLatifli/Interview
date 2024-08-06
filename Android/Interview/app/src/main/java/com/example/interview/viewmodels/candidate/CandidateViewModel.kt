package com.example.interview.viewmodels.candidate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.candidatedocument.CandidateDocumentResponse
import com.example.interview.models.responses.get.candidate.CandidateResponse
import com.example.interview.models.responses.post.candidatedocument.CandidateDocumentRequest
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.candidate.CandidateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor(private val candidateRepository: CandidateRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _complateResult = MutableLiveData<Boolean>()
    val complateResult: LiveData<Boolean> = _complateResult

    private val _afterdeleteResult = MutableLiveData<Boolean>()
    val afterdeleteResult: LiveData<Boolean> = _afterdeleteResult

    private val _afterupdateResult = MutableLiveData<Boolean>()
    val afterupdateResult: LiveData<Boolean> = _afterupdateResult

    private val _candidateDocuments = MutableLiveData<List<CandidateDocumentResponse>>()
    val candidateDocuments: LiveData<List<CandidateDocumentResponse>> = _candidateDocuments

    private val _candidateDocument = MutableLiveData<CandidateDocumentResponse>()
    val candidateDocument: LiveData<CandidateDocumentResponse> = _candidateDocument

    private val _candidates = MutableLiveData<List<CandidateResponse>>()
    val candidates: LiveData<List<CandidateResponse>> = _candidates

    fun getAllCandidateDocuments(): List<CandidateDocumentResponse> {
        _loading.value = true
        viewModelScope.launch {
            delay(1000)
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
                Log.e("CandidateViewModel", result.message ?: "Unknown error")
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
                Log.e("CandidateViewModel", result.message ?: "Unknown error")
            }
        }

    }

    fun getAllCandidate() {
        _loading.value = true

        viewModelScope.launch {
            val result = candidateRepository.getAllCandidates()
            if (result is Resource.Success) {
                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _candidates.value = itemResponse!!
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("CandidateViewModel", result.message ?: "Unknown error")
            }
        }
    }



    fun getLastCandidateDocument(): CandidateDocumentResponse? {
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
                Log.e("CandidateViewModel", result.message ?: "Unknown error")
            }
        }

        return _candidateDocuments.value?.lastOrNull()
    }




    fun registerCandidatedocument(candidateDocumentRequest: CandidateDocumentRequest) {
        _loading.value = true

        viewModelScope.launch {
            val result = candidateRepository.registerCandidatedocument(candidateDocumentRequest)
            if (result is Resource.Success) {

                delay(500)

                _complateResult.postValue(true)
                    Log.d("CandidateViewModel", "CandidateDocument registered: ${result.data}")

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _complateResult.postValue(false)

                Log.e("CandidateViewModel", result.message ?: "Unknown error")
            }
        }
    }

    fun updateCandidateDocument(id: Int, candidateDocumentRequest: CandidateDocumentRequest) {
        _loading.value = true

        viewModelScope.launch {
            val result = candidateRepository.updateCandidateDocument(id, candidateDocumentRequest)
            if (result is Resource.Success) {
                delay(500)
                _afterupdateResult.postValue(true)
                Log.d("CandidateViewModel", "CandidateDocument updated: ${result.data}")
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _afterupdateResult.postValue(false)
                Log.e("CandidateViewModel", result.message ?: "Unknown error")
            }
        }
    }



    fun deleteCandidateDocumentById(documentId: Int) {
        _loading.value = true

        viewModelScope.launch {
            val result = candidateRepository.deleteCandidateDocumentById(documentId)
            if (result is Resource.Success) {

                delay(500)
                _loading.postValue(false)
                _afterdeleteResult.postValue(true)
                Log.d("CandidateViewModel", "CandidateDocument deleted successfully")
            } else if (result is Resource.Error) {
                _afterdeleteResult.postValue(false)
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("CandidateViewModel", "Failed to delete CandidateDocument: ${result.message ?: "Unknown error"}")
            }
        }
    }


}




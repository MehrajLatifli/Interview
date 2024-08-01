package com.example.interview.viewmodels.vacancy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.candidate.CandidateDocumentResponse
import com.example.interview.models.responses.get.position.PositionResponse
import com.example.interview.models.responses.get.structure.StructureResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.models.responses.post.candidatedocument.CandidateDocument
import com.example.interview.models.responses.post.vacancy.Vacancy
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.position.PositionRepository
import com.example.interview.source.api.repositories.structure.StructureRepository
import com.example.interview.source.api.repositories.vacancy.VacancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val structureRepository: StructureRepository,
    private val positionRepository: PositionRepository,
    private val vacancyRepository: VacancyRepository
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

    private val _vacancies = MutableLiveData<List<VacancyResponse>>()
    val vacancies: LiveData<List<VacancyResponse>> = _vacancies

    private val _vacancy = MutableLiveData<VacancyResponse>()
    val vacancy: LiveData<VacancyResponse> = _vacancy

    private val _structures = MutableLiveData<List<StructureResponse>>()
    val structures: LiveData<List<StructureResponse>> = _structures

    private val _structure = MutableLiveData<StructureResponse>()
    val structure: LiveData<StructureResponse> = _structure

    private val _positions = MutableLiveData<List<PositionResponse>>()
    val positions: LiveData<List<PositionResponse>> = _positions

    private val _position = MutableLiveData<PositionResponse>()
    val position: LiveData<PositionResponse> = _position


    fun addVacancy(vacancy: Vacancy) {
        _loading.value = true

        viewModelScope.launch {
            val result = vacancyRepository.addVacancy(vacancy)
            if (result is Resource.Success) {

                delay(500)

                _completeResult.postValue(true)
                Log.d("VacancyViewModel", "VacancyViewModel created: ${result.data}")

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _completeResult.postValue(false)

                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }
    }

    fun getAllVacancies(): List<VacancyResponse> {
        _loading.value = true
        viewModelScope.launch {
            val result = vacancyRepository.getAllVacancies()
            if (result is Resource.Success) {

                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _vacancies.value = itemResponse!!
                    Log.e("Vacancies",  _vacancies.value.toString())
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }

        return _vacancies.value.orEmpty()
    }

    fun getVacancyByID(id:Int) {
        _loading.value = true
        viewModelScope.launch {
            val result = vacancyRepository.getVacancyByID(id)
            if (result is Resource.Success) {

                delay(200)
                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _vacancy.value = itemResponse!!
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }

    }



    fun getAllPositions(): List<PositionResponse> {
        _loading.value = true
        viewModelScope.launch {
            val result = positionRepository.getAllPositions()
            if (result is Resource.Success) {

                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _positions.value = itemResponse!!
                    Log.e("Positions",  _positions.value.toString())
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }

        return _positions.value.orEmpty()
    }

    fun getPositionByID(id:Int):PositionResponse? {
        _loading.value = true
        viewModelScope.launch {
            val result = positionRepository.getPositionByID(id)
            if (result is Resource.Success) {

                delay(200)
                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _position.value = itemResponse!!
                    Log.e("Position",  _position.value.toString())
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }

        return _position.value?:null
    }

    fun getAllStructures(): List<StructureResponse> {
        _loading.value = true
        viewModelScope.launch {
            val result = structureRepository.getAllStructures()
            if (result is Resource.Success) {

                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _structures.value = itemResponse!!
                    Log.e("Structures",  _structures.value.toString())
                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }

        return _structures.value.orEmpty()
    }

    fun getStructureByID(id:Int) :StructureResponse?{
        _loading.value = true

        viewModelScope.launch {
            val result = structureRepository.getStructureByID(id)
            if (result is Resource.Success) {

                delay(200)
                _loading.postValue(false)
                val itemResponse = result.data
                if (itemResponse != null) {
                    _structure.value = itemResponse!!
                    Log.e("Structure",  _structure.value.toString())

                }

            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }

        return  _structure.value?:null
    }

    fun deleteVacancyById(documentId: Int) {
        _loading.value = true

        viewModelScope.launch {
            val result = vacancyRepository.deleteVacancyById(documentId)
            if (result is Resource.Success) {

                delay(500)
                _loading.postValue(false)
                _afterDeleteResult.postValue(true)
                Log.d("VacancyViewModel", "Vacancy deleted successfully")
            } else if (result is Resource.Error) {
                _afterDeleteResult.postValue(false)
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                Log.e("VacancyViewModel", "Failed to delete Vacancy: ${result.message ?: "Unknown error"}")
            }
        }
    }

    fun updateVacancy(vacancy: VacancyResponse) {
        _loading.value = true

        viewModelScope.launch {
            val result = vacancyRepository.updateVacancy(vacancy)
            if (result is Resource.Success) {
                delay(500)
                _afterUpdateResult.postValue(true)
                Log.d("VacancyViewModel", "Vacancy updated: ${result.data}")
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _afterUpdateResult.postValue(false)
                Log.e("VacancyViewModel", result.message ?: "Unknown error")
            }
        }
    }
}

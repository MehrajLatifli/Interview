package com.example.interview.viewmodels.vacancy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.position.PositionResponse
import com.example.interview.models.responses.get.structure.StructureResponse
import com.example.interview.models.responses.get.vacancy.VacancyResponse
import com.example.interview.models.responses.post.vacancy.VacancyRequest
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.position.PositionRepository
import com.example.interview.source.api.repositories.structure.StructureRepository
import com.example.interview.source.api.repositories.vacancy.VacancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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

    fun addVacancy(vacancyRequest: VacancyRequest) {
        _loading.value = true

        viewModelScope.launch {
            vacancyRepository.addVacancy(vacancyRequest).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(500) // Simulate network delay
                        _completeResult.postValue(true)
                        Log.d("VacancyViewModel", "Vacancy created: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        _completeResult.postValue(false)
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
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
                        Log.d("VacancyViewModel", "Vacancies fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
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
                        Log.d("VacancyViewModel", "Vacancy fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun getAllPositions() : List<PositionResponse> {
        _loading.value = true

        viewModelScope.launch {
            positionRepository.getAllPositions().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _loading.postValue(false)
                        _positions.postValue(result.data ?: emptyList())
                        Log.d("VacancyViewModel", "Positions fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }

        return _positions.value.orEmpty()
    }


    fun getPositionByID(id: Int) {
        _loading.value = true

        viewModelScope.launch {
            positionRepository.getPositionByID(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(200) // Simulate network delay
                        _loading.postValue(false)
                        _position.postValue(result.data!!)
                        Log.d("VacancyViewModel", "Position fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }
    }


    fun getAllStructures() : List<StructureResponse> {
        _loading.value = true

        viewModelScope.launch {
            structureRepository.getAllStructures().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _loading.postValue(false)
                        _structures.postValue(result.data ?: emptyList())
                        Log.d("VacancyViewModel", "Structures fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }

        return _structures.value.orEmpty()

    }

    fun getStructureByID(id: Int) {
        _loading.value = true

        viewModelScope.launch {
            structureRepository.getStructureByID(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(200) // Simulate network delay
                        _loading.postValue(false)
                        _structure.postValue(result.data!!)
                        Log.d("VacancyViewModel", "Structure fetched: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun deleteVacancyById(id: Int) {
        _loading.value = true

        viewModelScope.launch {
            vacancyRepository.deleteVacancyById(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(500) // Simulate network delay
                        _loading.postValue(false)
                        _afterDeleteResult.postValue(true)
                        Log.d("VacancyViewModel", "Vacancy deleted successfully")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _afterDeleteResult.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", "Failed to delete vacancy: ${result.message ?: "Unknown error"}")
                    }
                }
            }
        }
    }

    fun updateVacancy(vacancy: VacancyResponse) {
        _loading.value = true

        viewModelScope.launch {
            vacancyRepository.updateVacancy(vacancy).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        delay(500) // Simulate network delay
                        _afterUpdateResult.postValue(true)
                        Log.d("VacancyViewModel", "Vacancy updated: ${result.data}")
                    }
                    is Resource.Error -> {
                        _loading.postValue(false)
                        _afterUpdateResult.postValue(false)
                        _error.postValue(result.message ?: "Unknown error")
                        Log.e("VacancyViewModel", result.message ?: "Unknown error")
                    }
                }
            }
        }
    }
}

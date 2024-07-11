package com.example.interview.viewmodels.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> = _authResult

    fun register(register: Register) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val result = authRepository.registerUser(register)
                if (result is Resource.Success) {
                    _authResult.postValue(true)
                } else if (result is Resource.Error) {
                    _error.postValue(result.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _error.postValue(e.localizedMessage ?: "Unknown error")
            } finally {
                _loading.postValue(false)
            }
        }
    }
}

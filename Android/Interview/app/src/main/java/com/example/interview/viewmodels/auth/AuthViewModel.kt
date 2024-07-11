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

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.registerUser(register).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _authResult.postValue(true)
                    }
                    is Resource.Error -> {
                        // Combine all errors into a single message
                        Log.e("Register",register.toString())
                        val errorMessage = result.errorResponse?.title ?: "Unknown error"
                        Log.e("AuthViewModel", "Error during registration: $errorMessage")
                        _error.postValue(errorMessage)
                    }
                }
                _loading.postValue(false)
            }
        }
    }
}

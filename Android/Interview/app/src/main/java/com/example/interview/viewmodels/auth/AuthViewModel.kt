package com.example.interview.viewmodels.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.entities.LoginEntity
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.post.login.Login
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.auth.AuthRepository
import com.example.interview.source.local.mapping.toLoginEntity
import com.example.interview.source.local.mapping.toLoginResponse
import com.example.interview.source.local.repositories.EntityRepository
import com.example.interview.utilities.Constants.API_KEY
import com.example.interview.utilities.decryptWithAsync
import com.example.interview.utilities.encryptWithAsync
import com.example.interview.utilities.generateAESKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val repoEntity: EntityRepository
) : ViewModel() {

    private val _loginResponses = MutableLiveData<List<LoginResponse>>()
    val loginResponses: LiveData<List<LoginResponse>> = _loginResponses

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> = _authResult

    val secretKey = generateAESKey()

    var _encryptedText = MutableLiveData<String>()
    val encryptedText: LiveData<String> = _encryptedText

    var _decryptedText = MutableLiveData<String>()
    val decryptedText: LiveData<String> = _decryptedText

    var _hash = MutableLiveData<String>()
    val hash: LiveData<String> = _hash

    fun registerAdmin(register: Register) {
        _loading.value = true

        viewModelScope.launch {
            val result = authRepository.registerAdmin(register)
            if (result is Resource.Success) {
                _authResult.postValue(true)
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _authResult.postValue(false)
            }
        }
    }

    fun registerHR(register: Register) {
        _loading.value = true

        viewModelScope.launch {
            val result = authRepository.registerHR(register)
            if (result is Resource.Success) {
                _authResult.postValue(true)
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _authResult.postValue(false)
            }
        }
    }

    fun login(login: Login, onApiKeyGenerated: (String) -> Unit) {
        _loading.value = true

        viewModelScope.launch {
            delay(2000)
            val result = authRepository.login(login.username, login.password)
            if (result is Resource.Success) {
                val token = result.data?.token
                val refreshToken = result.data?.refreshToken
                val expiration = result.data?.expiration
                Log.d("Login", "Token: $token, Refresh Token: $refreshToken, Expiration: $expiration")

                val itemResponse = result.data
                if (itemResponse != null) {
                    _loginResponses.value = listOf(itemResponse)
                    Log.e("_loginResponses", _loginResponses.value.toString())

                    delay(2000)
                    val (encryptedToken, hashResult) = encryptWithAsync(itemResponse.token, secretKey)

                    _encryptedText.value = encryptedToken
                    _hash.value = hashResult

                    val entity = itemResponse.toLoginEntity(login.username, encryptedToken)
                    Log.e("entity", entity.toString())

                    val response = entity.toLoginResponse()
                    _decryptedText.value = decryptWithAsync(response.token!!, _hash.value!!, secretKey)!!

                    API_KEY = _decryptedText.value!!
                    Log.e("Token", "${API_KEY}")

                    _authResult.postValue(true)

                    deleteAllEntity()
                    saveEntity(entity)

                    onApiKeyGenerated(API_KEY)
                } else {
                    _error.value = "No token found"
                    _loginResponses.value = emptyList()
                    Log.e("APIFailed", _error.value.toString())
                }
            } else if (result is Resource.Error) {
                _loading.postValue(false)
                _error.postValue(result.message ?: "Unknown error")
                _authResult.postValue(false)
            }
        }
    }

    private fun saveEntity(entity: LoginEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repoEntity.addWeatherEntity(entity)
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error adding login entity: ${e.message}")
            }
        }
    }

    private fun deleteAllEntity() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repoEntity.deleteAllUser()
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error delete login entity: ${e.message}")
            }
        }
    }
}

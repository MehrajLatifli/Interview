package com.example.interview.viewmodels.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.models.responses.get.profile.Role
import com.example.interview.models.responses.get.profile.UserClaim
import com.example.interview.source.api.RefreshTokenDetector
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.auth.AuthRepository
import com.example.interview.source.api.repositories.profile.ProfileRepository
import com.example.interview.source.local.repositories.EntityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(private val profileRepository: ProfileRepository, private val repoEntity: EntityRepository) : ViewModel() {


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _profiles = MutableLiveData<List<ProfileResponse>>()
    val profiles: LiveData<List<ProfileResponse>> = _profiles

    private val _userclaims = MutableLiveData<List<UserClaim>>()
    val userclaims: LiveData<List<UserClaim>> = _userclaims

    private val _roles = MutableLiveData<List<Role>>()
    val roles: LiveData<List<Role>> = _roles



    fun getprofile(): List<ProfileResponse> {
        _loading.value = true

        viewModelScope.launch {

            delay(2500)
            profileRepository.getprofile().collectLatest { response: Resource<ProfileResponse> ->



                when (response) {
                    is Resource.Success -> {
                        delay(200)
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


    fun logout() {
        _loading.value = true

        viewModelScope.launch {

            delay(200)
            profileRepository.logout().collectLatest { response: Resource<Unit> ->

                Log.e("logout", "Current user")

                deleteAllEntity()
            }
        }
    }

    private fun deleteAllEntity() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repoEntity.deleteAllUser()
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error delete weather entity: ${e.message}")
            }
        }
    }

}
package com.example.interview.viewmodels.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interview.models.responses.get.login.LoginResponse
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.source.api.Resource
import com.example.interview.source.api.repositories.auth.AuthRepository
import com.example.interview.source.api.repositories.profile.ProfileRepository
import com.example.interview.source.local.repositories.EntityRepository
import com.example.interview.utilities.Constants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
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


    fun getprofile() {
        _loading.value = true

        viewModelScope.launch {

            delay(2000)
                profileRepository.getprofile().collectLatest { response: Resource<ProfileResponse> ->

                    Log.e("profiles API_kEY", API_KEY )
                    when (response) {
                        is Resource.Success -> {
                            delay(200)
                            _loading.value = false
                            val itemResponse = response.data
                            if (itemResponse != null) {
                                _profiles.value = listOf(itemResponse)
                                Log.e("Profile", _profiles.value.toString())


                            } else {
                                _error.value = "No profile found"
                                _profiles.value = emptyList()
                                Log.e("APIFailed", _error.value.toString())
                            }
                        }
                        is Resource.Error -> {
                            _loading.value = false
                            _error.value = "Failed to fetch profile:${response.message} ${response.message}"
                            Log.e("APIFailed", _error.value.toString())

                        }
                    }
                }

        }
    }
}
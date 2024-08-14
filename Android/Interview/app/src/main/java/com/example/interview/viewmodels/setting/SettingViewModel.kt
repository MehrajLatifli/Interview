package com.example.interview.viewmodels.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel() {

    private val _primaryfontSize = MutableLiveData<Float>().apply { value = 1.0f }
    private val _secondaryfontSize = MutableLiveData<Float>().apply { value = 1.0f }

    val primaryfontSize: LiveData<Float> get() = _primaryfontSize
    val secondaryfontSize: LiveData<Float> get() = _secondaryfontSize

    fun setPrimaryFontSize(sizeMultiplier: Float) {
        _primaryfontSize.value = sizeMultiplier
    }

    fun setSecondaryFontSize(sizeMultiplier: Float) {
        _secondaryfontSize.value = sizeMultiplier
    }



}


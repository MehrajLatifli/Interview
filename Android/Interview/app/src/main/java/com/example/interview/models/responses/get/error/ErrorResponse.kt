package com.example.interview.models.responses.get.error


import com.google.gson.annotations.SerializedName


data class ErrorResponse(
    @SerializedName("date") val date: String? = null,
    @SerializedName("machine") val machine: String? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("user") val user: Any? = null
)

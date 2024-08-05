package com.example.interview.models.responses.post.session


import com.google.gson.annotations.SerializedName

data class SessionRequest(
    @SerializedName("endValue")
    val endValue: Int,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("vacancyId")
    val vacancyId: Int,
    @SerializedName("candidateId")
    val candidateId: Int,
    @SerializedName("userId")
    val userId: Int,
)
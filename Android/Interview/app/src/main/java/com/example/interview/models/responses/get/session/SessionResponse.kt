package com.example.interview.models.responses.get.session


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("endValue")
    val endValue: Double?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("vacancyId")
    val vacancyId: Int?,
    @SerializedName("candidateId")
    val candidateId: Int?,
    @SerializedName("userId")
    val userId: Int?,
): Parcelable
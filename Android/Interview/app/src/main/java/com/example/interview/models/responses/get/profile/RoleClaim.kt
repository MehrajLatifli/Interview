package com.example.interview.models.responses.get.profile


import com.google.gson.annotations.SerializedName

data class RoleClaim(
    @SerializedName("claimType")
    val claimType: String,
    @SerializedName("claimValue")
    val claimValue: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("roleId")
    val roleId: Int
)
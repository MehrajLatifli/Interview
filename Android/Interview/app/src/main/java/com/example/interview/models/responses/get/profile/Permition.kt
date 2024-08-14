package com.example.interview.models.responses.get.profile


import com.google.gson.annotations.SerializedName

data class Permition(
    @SerializedName("roleClaims")
    val roleClaims: List<RoleClaim>,
    @SerializedName("roles")
    val roles: List<Role>,
    @SerializedName("userClaims")
    val userClaims: List<UserClaim>
)
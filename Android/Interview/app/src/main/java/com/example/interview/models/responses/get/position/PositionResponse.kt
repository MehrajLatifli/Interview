package com.example.interview.models.responses.get.position


import com.google.gson.annotations.SerializedName

data class PositionResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
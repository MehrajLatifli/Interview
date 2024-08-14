package com.example.interview.models.responses.get.structure


import com.google.gson.annotations.SerializedName

data class StructureResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("parentId")
    val parentId: String,
    @SerializedName("structureTypeId")
    val structureTypeId: Int
)
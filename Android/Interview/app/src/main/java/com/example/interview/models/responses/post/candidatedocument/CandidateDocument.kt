package com.example.interview.models.responses.post.candidatedocument


import com.google.gson.annotations.SerializedName

data class CandidateDocumentItem(
    @SerializedName("address")
    val address: String,
    @SerializedName("cv")
    val cv: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("patronymic")
    val patronymic: String,
    @SerializedName("phonenumber")
    val phonenumber: String,
    @SerializedName("surname")
    val surname: String
)
package com.example.interview.models.responses.post.candidatedocument


import com.google.gson.annotations.SerializedName
import java.io.File

data class CandidateDocument(
    @SerializedName("address")
    val address: String?,
    @SerializedName("cv")
    val cv: File? = null,
    @SerializedName("email")
    val email: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("patronymic")
    val patronymic: String?,
    @SerializedName("phonenumber")
    val phonenumber: String?,
    @SerializedName("surname")
    val surname: String?
)
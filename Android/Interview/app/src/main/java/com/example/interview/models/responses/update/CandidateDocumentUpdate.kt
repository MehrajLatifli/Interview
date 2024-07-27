package com.example.interview.models.responses.update

import com.google.gson.annotations.SerializedName
import java.io.File

data class CandidateDocumentUpdate(
    @SerializedName("id")
    val id: Int?,
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
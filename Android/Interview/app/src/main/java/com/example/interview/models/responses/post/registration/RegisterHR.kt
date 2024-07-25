package com.example.interview.models.responses.post.registration

import com.google.gson.annotations.SerializedName
import java.io.File

data class RegisterHR (
    @SerializedName("username")
    val username: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("imagePath")
    val imagePath: File?
)
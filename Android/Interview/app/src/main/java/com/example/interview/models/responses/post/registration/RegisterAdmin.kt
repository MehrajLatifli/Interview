package com.example.interview.models.responses.post.registration

import java.io.File

data class Register(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val phoneNumber: String? = null,
    val imagePath: File? = null
)

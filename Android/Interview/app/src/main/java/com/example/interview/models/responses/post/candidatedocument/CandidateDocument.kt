package com.example.interview.models.responses.post.candidatedocument

import java.io.File

data class CandidateDocument (
    val surname: String? = null,
    val name: String? = null,
    val patronymic: String? = null,
    val phonenumber: String? = null,
    val email: String? = null,
    val cv: File? = null,
    val address: String? = null,
)
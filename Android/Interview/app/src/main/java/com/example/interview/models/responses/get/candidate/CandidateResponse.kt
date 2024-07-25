package com.example.interview.models.responses.get.candidate


import com.google.gson.annotations.SerializedName

data class Candidate(
    @SerializedName("CandidateDocumentId")
    val candidateDocumentId: Int
)
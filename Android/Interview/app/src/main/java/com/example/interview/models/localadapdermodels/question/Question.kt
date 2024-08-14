package com.example.interview.models.localadapdermodels.question

import com.example.interview.models.localadapdermodels.operationcrud.Operation
import com.example.interview.models.localadapdermodels.questionvalue.QuestionValue

data class Question (
    val id: Int,
    val sessionQuestionid: Int,
    val image: Int,
    val text: String,
    val questionvalues: List<QuestionValue>
)
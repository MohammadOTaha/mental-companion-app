package com.omar.mentalcompanion.presentation.screens.questionnaire.states

data class QuestionnaireState(
    val questionNumber: Int = 0,
    val selectedAnswers: MutableList<Int> = MutableList(10) { -1 },
)
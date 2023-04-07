package com.omar.mentalcompanion.presentation.screens.questionnaire.events

sealed class QuestionnaireEvent {
    object NextQuestion : QuestionnaireEvent()
    object PreviousQuestion : QuestionnaireEvent()
    data class SelectAnswer(val questionNumber: Int, val answerNumber: Int) : QuestionnaireEvent()
}

package com.omar.mentalcompanion.presentation.screens.questionnaire.utils

class Answers {
    companion object {
        const val A0 = "Not at all"
        const val A1 = "Several days"
        const val A2 = "More than half the days"
        const val A3 = "Nearly every day"

        fun getAll(): List<String> {
            return listOf(A0, A1, A2, A3)
        }
    }
}
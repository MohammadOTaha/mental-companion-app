package com.omar.mentalcompanion.presentation.screens.questionnaire.utils

class Questions {
    companion object {
        private const val Q0 = "How often have you been bothered by the following over the past week?"
        private const val Q1 = "Had little interest or pleasure in doing things?"
        private const val Q2 = "Felt down, depressed, or hopeless?"
        private const val Q3 = "Had trouble falling or staying asleep, or sleeping too much?"
        private const val Q4 = "Felt tired or having little energy?"
        private const val Q5 = "Had poor appetite or overeating?"
        private const val Q6 = "Felt bad about yourself - or that you are a failure or have let yourself or your family down?"
        private const val Q7 = "Had trouble concentrating on things, such as reading the newspaper or watching television?"
        private const val Q8 = "Moved or spoke so slowly that other people could have noticed? Or the opposite - being so fidgety or restless that you have been moving around a lot more than usual?"
        private const val Q9 = "Had thoughts that you would be better off dead, or of hurting yourself in some way?"
        private const val Q10 = "How many hours of sleep did you get last night?"

        fun get(questionNumber: Int): String {
            return when (questionNumber) {
                0 -> Q0
                1 -> Q1
                2 -> Q2
                3 -> Q3
                4 -> Q4
                5 -> Q5
                6 -> Q6
                7 -> Q7
                8 -> Q8
                9 -> Q9
                10 -> Q10
                else -> throw IllegalArgumentException("Question number must be between 0 and 9")
            }
        }
    }
}
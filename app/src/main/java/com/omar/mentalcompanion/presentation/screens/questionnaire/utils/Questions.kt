package com.omar.mentalcompanion.presentation.screens.questionnaire.utils

class Questions {
    companion object {
        const val Q0 = "How often have you been bothered by the following over the past 2 weeks?"
        const val Q1 = "1. Had little interest or pleasure in doing things?"
        const val Q2 = "2. Felt down, depressed, or hopeless?"
        const val Q3 = "3. Had trouble falling or staying asleep, or sleeping too much?"
        const val Q4 = "4. Felt tired or having little energy?"
        const val Q5 = "5. Had poor appetite or overeating?"
        const val Q6 = "6. Felt bad about yourself - or that you are a failure or have let yourself or your family down?"
        const val Q7 = "7. Had trouble concentrating on things, such as reading the newspaper or watching television?"
        const val Q8 = "8. Moved or spoke so slowly that other people could have noticed? Or the opposite - being so fidgety or restless that you have been moving around a lot more than usual?"
        const val Q9 = "9. Had thoughts that you would be better off dead, or of hurting yourself in some way?"

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
                else -> throw IllegalArgumentException("Question number must be between 0 and 9")
            }
        }
    }
}
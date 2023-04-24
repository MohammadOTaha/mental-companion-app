package com.omar.mentalcompanion.presentation.screens

import androidx.navigation.NamedNavArgument

sealed class ActiveScreen(val route: String, val args: List<NamedNavArgument> = emptyList()) {

    object IntroductionScreen: ActiveScreen("introduction_screen")

    object WelcomeBackScreen: ActiveScreen("welcome_screen")

    object CollectedDataScreen: ActiveScreen("collected_data_screen")

    object QuestionnaireScreen: ActiveScreen("questionnaire_screen")

    object SleepQuestionScreen: ActiveScreen("sleep_question_screen")
}
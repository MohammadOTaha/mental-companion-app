package com.omar.mentalcompanion.presentation.screens

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class ActiveScreen(val route: String, val args: List<NamedNavArgument> = emptyList()) {

    object WelcomeBackScreen: ActiveScreen("welcome_screen")

    object CollectedDataScreen: ActiveScreen("collected_data_screen")

    object QuestionnaireScreen: ActiveScreen("questionnaire_screen", listOf(
        navArgument("questionNumber") {
            type = NavType.IntType
            defaultValue = 0
            nullable = false
        }
    ))

    fun getRouteWithArgs(vararg args: String): String {
        var route = route
        args.forEach {
            route += "/$it"
        }
        return route
    }
}
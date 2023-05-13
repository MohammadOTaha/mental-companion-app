package com.omar.mentalcompanion.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.omar.mentalcompanion.data.services.SyncService
import com.omar.mentalcompanion.domain.services.LocationBackgroundService
import com.omar.mentalcompanion.domain.utils.*
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.collected_data.CollectedDataScreen
import com.omar.mentalcompanion.presentation.screens.introduction.IntroductionScreen
import com.omar.mentalcompanion.presentation.screens.questionnaire.QuestionScreen
import com.omar.mentalcompanion.presentation.screens.welcome_back.WelcomeScreen
import com.omar.mentalcompanion.presentation.ui.theme.MentalCompanionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var syncService: SyncService

    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        setContent {
            MentalCompanionTheme {
                val mainViewModel = hiltViewModel<MainViewModel>()

                init(mainViewModel)

                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = runBlocking { mainViewModel.getDestination() }
                ) {
                    composable(route = ActiveScreen.IntroductionScreen.route) {
                        IntroductionScreen(
                            navController = navController,
                            startLocationBackgroundService = { this@MainActivity.startLocationBackgroundService() }
                        )
                    }

                    composable(route = ActiveScreen.WelcomeBackScreen.route) {
                        WelcomeScreen(navController = navController)
                    }

                    composable(route = ActiveScreen.CollectedDataScreen.route) {
                        CollectedDataScreen(navController = navController)
                    }

                    composable(route = ActiveScreen.QuestionnaireScreen.route) {
                        QuestionScreen(
                            navController = navController,
                            questionNumber = 0
                        )
                    }

                    composable(route = ActiveScreen.SleepQuestionScreen.route) {
                        QuestionScreen(
                            navController = navController,
                            questionNumber = 10
                        )
                    }
                }
            }
        }
    }

    private fun init(mainViewModel: MainViewModel) {
        mainViewModel.initMetaData()

        mainViewModel.scheduleReminderNotification(
            SLEEP_QUESTION_HOUR,
            SLEEP_QUESTION_MINUTE,
            NOTIFICATION_TITLE,
            SLEEP_QUESTION_CONTENT
        )

//        mainViewModel.scheduleReminderNotification(
//            QUESTIONNAIRE_REMINDER_HOUR,
//            QUESTIONNAIRE_REMINDER_MINUTE,
//            NOTIFICATION_TITLE,
//            QUESTIONNAIRE_REMINDER_CONTENT
//        )

        mainViewModel.scheduleSyncDataWorker(0, 0)

        if (runBlocking { mainViewModel.getIsIntroductionCompleted() }) {
            startLocationBackgroundService()
        }

    }

    private fun startLocationBackgroundService() {
        Intent(applicationContext, LocationBackgroundService::class.java).apply {
            action = LocationBackgroundService.ACTION_START
            startService(this)
        }
    }

    private fun requestPermissions() {
        val permissionsList = emptyList<String>().toMutableList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
            ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), 0)
        }
    }
}
package com.omar.mentalcompanion.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.core.app.ActivityCompat
import com.omar.mentalcompanion.presentation.ui.theme.MentalCompanionTheme
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.questionnaire.QuestionScreen
import dagger.hilt.android.AndroidEntryPoint
import android.provider.Settings
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.*
import com.omar.mentalcompanion.data.services.SyncService
import com.omar.mentalcompanion.presentation.screens.collected_data.CollectedDataScreen
import com.omar.mentalcompanion.presentation.screens.welcome_back.WelcomeScreen
import javax.inject.Inject
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


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
                val viewModel = hiltViewModel<MainViewModel>()

                init(viewModel)

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        viewModel.permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = viewModel.getStartDestination()
                ) {
                    composable(route = ActiveScreen.WelcomeBackScreen.route) {
                        WelcomeScreen(navController = navController)
                    }

                    composable(route = ActiveScreen.CollectedDataScreen.route) {
                        CollectedDataScreen(navController = navController)

//                        Button(onClick = {
//                            multiplePermissionResultLauncher.launch(viewModel.permissionsToRequest)
//                        }) {
//                            Text(text = "Request multiple permission")
//                        }
//
//                        viewModel.visiblePermissionDialogQueue
//                            .reversed()
//                            .forEach { permission ->
//                                PermissionDialog(
//                                    permissionTextProvider = when (permission) {
//                                        Manifest.permission.ACCESS_FINE_LOCATION -> {
//                                            GpsPermissionTextProvider()
//                                        }
//                                        Manifest.permission.READ_CALL_LOG -> {
//                                            PhoneCallLogPermissionTextProvider()
//                                        }
//                                        else -> return@forEach
//                                    },
//                                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
//                                    onDismiss = viewModel::dismissDialog,
//                                    onOkClick = {
//                                        viewModel.dismissDialog()
//                                        multiplePermissionResultLauncher.launch(
//                                            arrayOf(permission)
//                                        )
//                                    },
//                                    onGoToAppSettingsClick = ::openAppSettings
//                                )
//                            }
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(MaterialTheme.colorScheme.background),
//                            verticalArrangement = Arrangement.spacedBy(16.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceAround
//                            ) {
//                                Button(onClick = {
//                                    Intent(
//                                        applicationContext,
//                                        BackgroundService::class.java
//                                    ).apply {
//                                        action = BackgroundService.ACTION_START
//                                        startService(this)
//                                    }
//                                }) {
//                                    Text(text = "Start Service")
//                                }
//
//                                Button(onClick = {
//                                    Intent(
//                                        applicationContext,
//                                        BackgroundService::class.java
//                                    ).apply {
//                                        action = BackgroundService.ACTION_STOP
//                                        startService(this)
//                                    }
//                                }) {
//                                    Text(text = "Stop Service")
//                                }
//                            }
//
//                            CollectedDataScreen(navController = navController)
//                        }
                    }

                    composable(
                        route = ActiveScreen.QuestionnaireScreen.route + "/{questionNumber}",
                        arguments = ActiveScreen.QuestionnaireScreen.args
                    ) {
                        QuestionScreen(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }

    private fun init(viewModel: MainViewModel) {
        viewModel.initMetaData()
    }

    private fun requestPermissions() {
        val permissionsList = emptyList<String>().toMutableList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), 0)

//        val usageStatsData: UsageStatsData by lazy { UsageStatsData(applicationContext) }
//        if (usageStatsData.getAppUsages().isEmpty()) {
//            this.startActivity(Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS))
//        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
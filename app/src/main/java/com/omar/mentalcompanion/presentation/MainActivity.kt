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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omar.mentalcompanion.domain.tracked_data.UsageStatsData
import com.omar.mentalcompanion.presentation.screens.collected_data.CollectedDataScreen
import com.omar.mentalcompanion.presentation.ui.theme.MentalCompanionTheme
import com.omar.mentalcompanion.domain.services.BackgroundService
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.questionnaire.QuestionScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel
import android.provider.Settings
import android.net.Uri


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CALL_LOG,
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.init()

        setContent {
            MentalCompanionTheme {
                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            viewModel.onPermissionResult(
                                permission = permission,
                                isGranted = perms[permission] == true
                            )
                        }
                    }
                )

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ActiveScreen.CollectedDataScreen.route
                ) {
                    composable(route = ActiveScreen.CollectedDataScreen.route) {
                        Button(onClick = {
                            multiplePermissionResultLauncher.launch(permissionsToRequest)
                        }) {
                            Text(text = "Request multiple permission")
                        }

                        dialogQueue
                            .reversed()
                            .forEach { permission ->
                                PermissionDialog(
                                    permissionTextProvider = when (permission) {
                                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                                            GpsPermissionTextProvider()
                                        }
                                        Manifest.permission.READ_CALL_LOG -> {
                                            PhoneCallLogPermissionTextProvider()
                                        }
                                        else -> return@forEach
                                    },
                                    isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                                    onDismiss = viewModel::dismissDialog,
                                    onOkClick = {
                                        viewModel.dismissDialog()
                                        multiplePermissionResultLauncher.launch(
                                            arrayOf(permission)
                                        )
                                    },
                                    onGoToAppSettingsClick = ::openAppSettings
                                )
                            }
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

    private fun init() {
//        requestPermissions()
    }

    private fun requestPermissions() {
        val permissionsList = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CALL_LOG,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), 0)

        val usageStatsData: UsageStatsData by lazy { UsageStatsData(applicationContext) }
        if (usageStatsData.getAppUsages().isEmpty()) {
            this.startActivity(Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}
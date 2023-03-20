package com.omar.mentalcompanion.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.omar.mentalcompanion.AppViewModel
import com.omar.mentalcompanion.services.BackgroundService
import com.omar.mentalcompanion.presentation.collected_data.CollectedDataList
import com.omar.mentalcompanion.presentation.ui.theme.MentalCompanionTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val applicationViewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.init()

        setContent {
            MentalCompanionTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(onClick = {
                            Intent(applicationContext, BackgroundService::class.java).apply {
                                action = BackgroundService.ACTION_START
                                startService(this)
                            }
                        }) {
                            Text(text = "Start Service")
                        }

                        Button(onClick = {
                            Intent(applicationContext, BackgroundService::class.java).apply {
                                action = BackgroundService.ACTION_STOP
                                startService(this)
                            }
                        }) {
                            Text(text = "Stop Service")
                        }
                    }

                    CollectedDataList()
                }
            }
        }
    }

    private fun init() {
        requestPermissions()

        applicationViewModel.startLocationUpdates()
    }

    private fun requestPermissions() {
        val permissionsList = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        ActivityCompat.requestPermissions(this, permissionsList.toTypedArray(), 0)
    }
}
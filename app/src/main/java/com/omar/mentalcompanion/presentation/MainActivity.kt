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
import com.omar.mentalcompanion.domain.tracked_data.UsageStatsData
import com.omar.mentalcompanion.presentation.collected_data.CollectedDataList
import com.omar.mentalcompanion.presentation.ui.theme.MentalCompanionTheme
import com.omar.mentalcompanion.domain.services.BackgroundService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
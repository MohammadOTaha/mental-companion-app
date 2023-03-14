package com.omar.mentalcompanion

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.omar.mentalcompanion.services.BackgroundService
import com.omar.mentalcompanion.services.UsageStatsService
import com.omar.mentalcompanion.ui.theme.MentalCompanionTheme

class MainActivity : ComponentActivity() {

    private lateinit var locationClient: LocationClient
    private lateinit var usageStatsService: UsageStatsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.init()

        setContent {
            MentalCompanionTheme {
                val locationLiveData = locationClient.getLocationLiveData(5000L)
                usageStatsService = remember { UsageStatsService(this) }

                var trackedData by remember {
                    mutableStateOf(
                        mapOf(
                            "Location" to "...",
                            "App Usages" to usageStatsService.getAppUsages(),
                            "Screen Time" to usageStatsService.getTotalScreenTime()
                        )
                    )
                }

                locationLiveData.observe(this) {
                    trackedData = trackedData.toMutableMap().apply {
                        this["Location"] = "${it.latitude}, ${it.longitude}"
                    }
                }

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

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TableScreen(
                            data = trackedData
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = {
                                trackedData = trackedData.toMutableMap().apply {
                                    this["App Usages"] = usageStatsService.getAppUsages()
                                    this["Screen Time"] = usageStatsService.getTotalScreenTime()
                                }
                            }) {
                                Text(text = "Refresh")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TableScreen(data: Map<String, Any>) {
        LazyColumn(
            modifier = Modifier.padding(PaddingValues(horizontal = 12.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach { (name, value) ->
                item {
                    CollectedDataItem(
                        name = name,
                        value = value.toString()
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @Composable
    private fun CollectedDataItem(name: String, value: String) {
        Card(shape = MaterialTheme.shapes.small) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = name, style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        }
    }

    private fun init() {
        requestPermissions()

        locationClient = LocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        usageStatsService = UsageStatsService(this)
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
package com.omar.mentalcompanion

import android.Manifest
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import com.omar.mentalcompanion.ui.theme.MentalCompanionTheme


class MainActivity : ComponentActivity() {

    private lateinit var locationClient: LocationClient

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initApp()

        // check if user has granted permission to access usage stats
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats: List<UsageStats> = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time)
        if (stats.isEmpty()) {
            startActivity(Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        var appUsages: String = "";
        for (stat in stats) {
            if (stat.totalTimeInForeground > 0) {
                appUsages += "${stat.packageName} : ${stat.totalTimeInForeground} ms --- ${stat.lastTimeUsed}\n\n"
            }
        }

        locationClient = LocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        setContent {
            MentalCompanionTheme {
                val locationLiveData = locationClient.getLocationLiveData(5000L)
                var locationData by remember {
                    mutableStateOf(
                        listOf(
                            "Location" to "...",
                            "App Usages" to appUsages
                        )
                    )
                }

                LaunchedEffect(locationLiveData) {
                    locationLiveData.observe(this@MainActivity) { location ->
                        locationData = listOf(
                            "Location" to location.latitude.toString() + ", " + location.longitude.toString(),
                            "App Usages" to appUsages
                        )
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
                            data = locationData
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TableScreen(data: List<Pair<String, String>>) {
        LazyColumn(
            modifier = Modifier.padding(PaddingValues(horizontal = 12.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data.size) { index ->
                CollectedDataItem(name = data[index].first, value = data[index].second)
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

    private fun initApp() {
        requestPermissions()
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
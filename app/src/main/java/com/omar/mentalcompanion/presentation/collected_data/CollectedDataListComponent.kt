package com.omar.mentalcompanion.presentation.collected_data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omar.mentalcompanion.AppViewModel
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CollectedDataList (modifier: Modifier = Modifier) {
    val applicationViewModel = hiltViewModel<AppViewModel>()
    val location by applicationViewModel.locationLiveData.observeAsState()
    val totalScreenTime = applicationViewModel.totalScreenTime.collectAsState()
    val usageStatsList = applicationViewModel.appUsageList.collectAsState()
    val phoneCallsLog = applicationViewModel.phoneCallsLog.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    applicationViewModel.updateLocation()
                    applicationViewModel.updateAppUsageList()
                    applicationViewModel.updateTotalScreenTime()
                    applicationViewModel.updatePhoneCallsLog()
                }) {
                    Text(text = "Refresh")
                }
            }

            TableScreen(
                data = mapOf(
                    "Location" to "${location?.latitude}, ${location?.longitude}",
                    "Total Screen Time" to totalScreenTime.value,
                    "Usage Stats" to usageStatsList.value,
                    "Phone Calls Log" to phoneCallsLog.value
                )
            )
        }
    }
}

@Composable
private fun TableScreen(data: Map<String, Any>) {
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
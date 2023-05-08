package com.omar.mentalcompanion.presentation.screens.collected_data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omar.mentalcompanion.AppViewModel
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omar.mentalcompanion.data.entities.ApplicationUsage
import com.omar.mentalcompanion.domain.utils.toFormattedTimeString
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.collected_data.viewmodels.ApplicationUsageViewModel

@Composable
fun CollectedDataScreen (
    navController: NavController
) {
    val applicationViewModel = hiltViewModel<AppViewModel>()
    val appUsageViewModel = hiltViewModel<ApplicationUsageViewModel>()
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
                    applicationViewModel.updateAppUsageList()
                    applicationViewModel.updateTotalScreenTime()
                    applicationViewModel.updatePhoneCallsLog()

                    for (usage in usageStatsList.value) {
                        appUsageViewModel.upsertApplicationUsage(ApplicationUsage(usage.packageName, usage.usageTime))
                    }
                }) {
                    Text(text = "Refresh")
                }

                Button(onClick = {
                    navController.navigate(ActiveScreen.QuestionnaireScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Go to Questionnaire")
                }
            }

            TableScreen(
                data = mapOf(
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
                text = name, style = MaterialTheme.typography.titleLarge,
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
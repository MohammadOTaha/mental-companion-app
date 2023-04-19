package com.omar.mentalcompanion.presentation.screens.welcome_back

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omar.mentalcompanion.presentation.MainViewModel
import com.omar.mentalcompanion.presentation.screens.ActiveScreen

@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val mainViewModel = hiltViewModel<MainViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // empty element to push the text to the bottom
        Spacer(modifier = Modifier)

        Text(
            text = "Hello!\nYour next questionnaire is in: ${mainViewModel.getTimeUntilNextQuestionnaire()} days.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(onClick = {
            navController.navigate(ActiveScreen.CollectedDataScreen.route)
        }) {
            Text(text = "Continue")
        }

        Text(
            text = "Thank you for beta testing the app ♥",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
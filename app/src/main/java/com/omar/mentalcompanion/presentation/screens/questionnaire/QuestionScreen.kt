package com.omar.mentalcompanion.presentation.screens.questionnaire

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.CommonComponents.ResponsiveText
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.Questions
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.Answers



@Composable
fun QuestionScreen(
    questionNumber: Int,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBDF384)),
        verticalArrangement = if (questionNumber == 0) Arrangement.Center else Arrangement.Top
    ) {
        if (questionNumber == 0) {
            HeaderQuestion(navController)
        } else {
            Question(questionNumber, navController)
        }
    }
}

@Composable
private fun HeaderQuestion(
    navController: NavController
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = Questions.get(0),
            style = MaterialTheme.typography.titleLarge,
        )

        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(ActiveScreen.QuestionnaireScreen.getRouteWithArgs("1"))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
private fun Question(
    questionNumber: Int,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        ResponsiveText(
            text = Questions.get(questionNumber),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 8
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Answers.getAll().forEach { answer ->
                    ExtendedFloatingActionButton(
                        onClick = {
                            navController.navigate(ActiveScreen.QuestionnaireScreen.getRouteWithArgs((questionNumber + 1).toString()))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                        ,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                        containerColor = Color.Transparent,
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(ActiveScreen.QuestionnaireScreen.getRouteWithArgs((questionNumber - 1).toString()))
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                containerColor = Color.Transparent,
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

    }
}
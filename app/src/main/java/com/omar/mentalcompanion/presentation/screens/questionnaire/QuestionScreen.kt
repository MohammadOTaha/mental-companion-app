package com.omar.mentalcompanion.presentation.screens.questionnaire

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.questionnaire.events.QuestionnaireEvent
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.Answers
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.CommonComponents.ResponsiveText
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.Questions
import com.omar.mentalcompanion.presentation.screens.questionnaire.viewmodels.QuestionnaireViewModel
import com.omar.mentalcompanion.presentation.utils.Constants.CONTENT_ANIMATION_DURATION


@Composable
fun QuestionScreen(
    viewModel: QuestionnaireViewModel = hiltViewModel(),
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBDF384)),
        verticalArrangement = if (viewModel.getQuestionNumber() == 0) Arrangement.Center else Arrangement.Top
    ) {
        AnimatedContent(
            targetState = viewModel.getQuestionNumber(),
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(CONTENT_ANIMATION_DURATION),
                    initialOffsetX = { fullWidth -> fullWidth }
                ).with(
                    slideOutHorizontally(
                        animationSpec = tween(CONTENT_ANIMATION_DURATION),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
                )
            }
        ) { questionNumber ->
            if (questionNumber == 0) {
                HeaderQuestion(viewModel)
            } else {
                Question(questionNumber, viewModel, navController)
            }
        }
    }
}

@Composable
private fun HeaderQuestion(
    viewModel: QuestionnaireViewModel
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = Questions.get(0),
            style = MaterialTheme.typography.titleLarge,
        )

        ExtendedFloatingActionButton(
            onClick = {
                viewModel.onEvent(QuestionnaireEvent.NextQuestion)
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
    viewModel: QuestionnaireViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        ResponsiveText(
            text = Questions.get(questionNumber),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 10
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Answers.getAll().forEach { answer ->
                    TextButton(
                        onClick = {
                            viewModel.onEvent(
                                QuestionnaireEvent.SelectAnswer(
                                    questionNumber,
                                    Answers.getAll().indexOf(answer)
                                )
                            )

                            if (questionNumber < 9) {
                                viewModel.onEvent(QuestionnaireEvent.NextQuestion)
                            } else {
                                viewModel.onEvent(QuestionnaireEvent.FinishQuestionnaire)
                                navController.navigate(ActiveScreen.WelcomeBackScreen.route)
                            }
                        },
                        modifier = Modifier
                            .padding(start = 0.dp),
                    ) {
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            color = Color(0xFF000000),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.onEvent(QuestionnaireEvent.PreviousQuestion)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                    containerColor = Color.Transparent,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "$questionNumber/9",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF000000),
                )
            }
        }

    }
}
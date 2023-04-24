package com.omar.mentalcompanion.presentation.screens.questionnaire

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omar.mentalcompanion.presentation.MainViewModel
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import com.omar.mentalcompanion.presentation.screens.questionnaire.events.QuestionnaireEvent
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.Answers
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.CommonComponents.ResponsiveText
import com.omar.mentalcompanion.presentation.screens.questionnaire.utils.Questions
import com.omar.mentalcompanion.presentation.screens.questionnaire.viewmodels.QuestionnaireViewModel
import com.omar.mentalcompanion.presentation.utils.Constants.CONTENT_ANIMATION_DURATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun QuestionScreen(
    questionnaireViewModel: QuestionnaireViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
    questionNumber: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = if (questionnaireViewModel.getQuestionNumber() == 0) Arrangement.Center else Arrangement.Top
    ) {
        if (questionNumber == 10) {
            SleepHoursQuestion(navController = navController, viewModel = questionnaireViewModel)
        } else {
            AnimatedContent(
                targetState = questionnaireViewModel.getQuestionNumber(),
                transitionSpec = {
                    if (initialState < targetState) {
                        slideInHorizontally(
                            animationSpec = tween(CONTENT_ANIMATION_DURATION),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ).with(
                            slideOutHorizontally(
                                animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                        )
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(CONTENT_ANIMATION_DURATION),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ).with(
                            slideOutHorizontally(
                                animationSpec = tween(CONTENT_ANIMATION_DURATION),
                                targetOffsetX = { fullWidth -> fullWidth }
                            )
                        )
                    }
                }
            ) { questionNumber ->
                if (questionNumber == 0) {
                    HeaderQuestion(questionnaireViewModel)
                } else if (questionNumber <= 9) {
                    QuestionnaireQuestion(questionNumber)
                    QuestionnaireAnswers(questionNumber, questionnaireViewModel, mainViewModel, navController)
                } else {
                    SleepHoursQuestion(navController = navController, viewModel = questionnaireViewModel)
                }
            }
        }
    }
}

@Composable
private fun HeaderQuestion(
    viewModel: QuestionnaireViewModel
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = Questions.get(0),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
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
private fun QuestionnaireQuestion(
    questionNumber: Int
) {
    Column(
        modifier = Modifier.padding(32.dp),
    ) {
        ResponsiveText(
            text = Questions.get(questionNumber),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 10
        )
    }
}

@Composable
private fun QuestionnaireAnswers(
    questionNumber: Int,
    questionnaireViewModel: QuestionnaireViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Column {
                Answers.getAll().forEach { answer ->
                    TextButton(
                        onClick = {
                            questionnaireViewModel.onEvent(
                                QuestionnaireEvent.SelectAnswer(
                                    questionNumber,
                                    Answers.getAll().indexOf(answer)
                                )
                            )

                            if (questionNumber < 9) {
                                questionnaireViewModel.onEvent(QuestionnaireEvent.NextQuestion)
                            } else {
                                questionnaireViewModel.onEvent(QuestionnaireEvent.FinishQuestionnaire)
                                navController.navigate(mainViewModel.getDestination())
                            }
                        },
                    ) {
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (answer != Answers.getAll().last()) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    questionnaireViewModel.onEvent(QuestionnaireEvent.PreviousQuestion)
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                containerColor = Color.Transparent,
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Text(
                text = "$questionNumber/9",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun SleepHoursQuestion(
    viewModel: QuestionnaireViewModel = hiltViewModel(),
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = Questions.get(10),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = "<--        Scroll to select        -->",
                fontSize = 25.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondary,
            )

            var selectedHour by remember { mutableStateOf(6) }
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)),
                color = Color.Transparent
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                        .height(72.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    state = listState
                ) {
                    items(24) { hour ->
                        SleepHourItem(
                            hour = hour,
                            selected = selectedHour == hour,
                            onClick = {
                                selectedHour = hour
                            }
                        )
                    }
                }

                LaunchedEffect(selectedHour) {
                    coroutineScope.launch {
                        listState.animateScrollAndCentralizeItem(selectedHour, this)
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onEvent(QuestionnaireEvent.SelectSleepHours(selectedHour))
                    navController.navigate(ActiveScreen.WelcomeBackScreen.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun LazyListState.animateScrollAndCentralizeItem(index: Int, scope: CoroutineScope) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    scope.launch {
        if (itemInfo != null) {
            val center = this@animateScrollAndCentralizeItem.layoutInfo.viewportEndOffset / 2
            val childCenter = itemInfo.offset + itemInfo.size / 2
            this@animateScrollAndCentralizeItem.animateScrollBy((childCenter - center).toFloat())
        } else {
            this@animateScrollAndCentralizeItem.animateScrollBy(730.0f)
        }
    }
}

@Composable
fun SleepHourItem(hour: Int, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = if (selected) 4.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "$hour",
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

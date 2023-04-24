package com.omar.mentalcompanion.presentation.screens.introduction

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.omar.mentalcompanion.presentation.screens.introduction.viewmodels.IntroductionViewModel
import com.omar.mentalcompanion.presentation.utils.Constants
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.omar.mentalcompanion.presentation.MainViewModel
import com.omar.mentalcompanion.presentation.screens.introduction.events.IntroductionPageEvent
import com.omar.mentalcompanion.presentation.screens.introduction.utils.IntroductionPageConstants

@Composable
fun IntroductionScreen(
    introductionViewModel: IntroductionViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = introductionViewModel.getPage(),
            transitionSpec = {
                if (initialState < targetState) {
                    slideInHorizontally(
                        animationSpec = tween(Constants.CONTENT_ANIMATION_DURATION),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ).with(
                        slideOutHorizontally(
                            animationSpec = tween(Constants.CONTENT_ANIMATION_DURATION),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        )
                    )
                } else {
                    slideInHorizontally(
                        animationSpec = tween(Constants.CONTENT_ANIMATION_DURATION),
                        initialOffsetX = { fullWidth -> -fullWidth }
                    ).with(
                        slideOutHorizontally(
                            animationSpec = tween(Constants.CONTENT_ANIMATION_DURATION),
                            targetOffsetX = { fullWidth -> fullWidth }
                        )
                    )
                }
            }
        ) {
            Page(introductionViewModel = introductionViewModel, mainViewModel = mainViewModel, navController = navController)
        }
    }
}

@Composable
private fun Page(
    introductionViewModel: IntroductionViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier)

        Text(
            text = IntroductionPageConstants.getPageText(introductionViewModel.getPage()),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (introductionViewModel.getPage() >= 5) Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 100.dp, end = 100.dp),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                text = "Allow",
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (introductionViewModel.getPage() != 0) {
                ExtendedFloatingActionButton(
                    onClick = {
                        introductionViewModel.onEvent(IntroductionPageEvent.PreviousPage)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                    containerColor = Color.Transparent,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                Spacer(modifier = Modifier)
            }

            ExtendedFloatingActionButton(
                onClick = {
                    if (introductionViewModel.getPage() >= IntroductionPageConstants.PAGES_COUNT - 1) {
                        introductionViewModel.onEvent(IntroductionPageEvent.FinishIntroduction)
                        navController.navigate(mainViewModel.getDestination())
                    } else {
                        introductionViewModel.onEvent(IntroductionPageEvent.NextPage)
                    }
                },
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                containerColor = Color.Transparent,
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "Next",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
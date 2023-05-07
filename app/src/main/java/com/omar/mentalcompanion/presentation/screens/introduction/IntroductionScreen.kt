package com.omar.mentalcompanion.presentation.screens.introduction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.omar.mentalcompanion.presentation.MainViewModel
import com.omar.mentalcompanion.presentation.screens.introduction.components.PermissionDialog
import com.omar.mentalcompanion.presentation.screens.introduction.events.IntroductionPageEvent
import com.omar.mentalcompanion.presentation.screens.introduction.utils.IntroductionPageConstants
import com.omar.mentalcompanion.presentation.screens.introduction.utils.goToAppSettings
import com.omar.mentalcompanion.presentation.screens.introduction.utils.isUsageAccessGranted
import com.omar.mentalcompanion.presentation.screens.introduction.viewmodels.IntroductionViewModel
import com.omar.mentalcompanion.presentation.utils.Constants
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun IntroductionScreen(
    introductionViewModel: IntroductionViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavController,
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
            Page(
                introductionViewModel = introductionViewModel,
                mainViewModel = mainViewModel,
                navController = navController
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Page(
    introductionViewModel: IntroductionViewModel,
    mainViewModel: MainViewModel,
    navController: NavController,
) {
    val permissionState =
        rememberPermissionState(IntroductionPageConstants.getPagePermission(introductionViewModel.getPage()))

    val showDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (showDialog.value) {
        PermissionDialog(
            permission = IntroductionPageConstants.getPagePermission(
                introductionViewModel.getPage()
            ),
            isPermanentlyDeclined = permissionState.status.shouldShowRationale,
            onDismiss = {
                introductionViewModel.setIsRequestPermissionButtonPressed(false)
            },
            onConfirm = {
                showDialog.value = false
            },
            onGoToAppSettingsClick = {
                goToAppSettings(context)

                showDialog.value = false
            },
            modifier = Modifier,
        )
    }

    if (introductionViewModel.getIsRequestPermissionButtonPressed() &&
        (permissionState.status.isGranted || (introductionViewModel.getPage() == 7 && isUsageAccessGranted(context)))
    ) {
        introductionViewModel.setIsRequestPermissionButtonPressed(false)

        if (introductionViewModel.getPage() >= IntroductionPageConstants.PAGES_COUNT - 1) {
            introductionViewModel.onEvent(IntroductionPageEvent.FinishIntroduction)
            navController.navigate(runBlocking { mainViewModel.getDestination() })
        } else {
            introductionViewModel.onEvent(IntroductionPageEvent.NextPage)
        }
    }

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

        if (introductionViewModel.getPage() >= 5) {
            if (introductionViewModel.getPage() == 7) {
                RequestUsageAccessPermissionButton(context)
            } else {
                RequestRegularPermissionButton(
                    introductionViewModel,
                    permissionState,
                    showDialog
                )
            }
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
                    if (
                        (introductionViewModel.getPage() == 7 && isUsageAccessGranted(context)) ||
                        permissionState.status.isGranted ||
                        introductionViewModel.getPage() < 5
                    ) {
                        if (introductionViewModel.getPage() >= IntroductionPageConstants.PAGES_COUNT - 1) {
                            introductionViewModel.onEvent(IntroductionPageEvent.FinishIntroduction)
                            navController.navigate(runBlocking { mainViewModel.getDestination() })
                        } else {
                            introductionViewModel.onEvent(IntroductionPageEvent.NextPage)
                        }
                    } else {
                        showDialog.value = true
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

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun RequestRegularPermissionButton(
    introductionViewModel: IntroductionViewModel,
    permissionState: PermissionState,
    showDialog: MutableState<Boolean>
) {
    Button(
        onClick = {
            if (!permissionState.status.shouldShowRationale) {
                permissionState.launchPermissionRequest()

                introductionViewModel.onEvent(
                    IntroductionPageEvent.RequestPermission(
                        permissionState.permission
                    )
                )
            } else {
                showDialog.value = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp),
        shape = MaterialTheme.shapes.small,
        enabled = !permissionState.status.isGranted
    ) {
        Text(
            text = if (permissionState.status.isGranted) {
                "Permission Granted"
            } else {
                "Allow Access"
            },
            modifier = Modifier,
            color = if (permissionState.status.isGranted) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            fontSize = if (permissionState.status.isGranted) {
                16.sp
            } else {
                20.sp
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun RequestUsageAccessPermissionButton(
    context: Context
) {
    val usageAccessPermissionState = remember { mutableStateOf(
        isUsageAccessGranted(context)
    ) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK || isUsageAccessGranted(context)) {
            usageAccessPermissionState.value = true
        }
    }

    Button(
        onClick = {
            launcher.launch(
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 50.dp),
        shape = MaterialTheme.shapes.small,
        enabled = !usageAccessPermissionState.value
    ) {
        Text(
            text = if (usageAccessPermissionState.value) {
                "Permission Granted"
            } else {
                "Allow Access"
            },
            modifier = Modifier,
            color = if (usageAccessPermissionState.value) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            fontSize = if (usageAccessPermissionState.value) {
                16.sp
            } else {
                20.sp
            }
        )
    }
}

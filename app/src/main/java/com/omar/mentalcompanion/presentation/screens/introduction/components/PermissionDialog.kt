package com.omar.mentalcompanion.presentation.screens.introduction.components

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PermissionDialog(
    permission: String,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val permissionTextProvider = when(permission) {
        Manifest.permission.ACCESS_FINE_LOCATION -> GpsPermissionTextProvider()
        Manifest.permission.PACKAGE_USAGE_STATS -> AppsUsagePermissionTextProvider()
        Manifest.permission.READ_CALL_LOG -> PhoneCallLogPermissionTextProvider()
        else -> throw IllegalArgumentException("Unknown permission: $permission")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider()
                Text(
                    text = if(isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "OK"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onConfirm()
                            }
                        }
                        .padding(16.dp)
                )
            }
        },
        title = {
            Text(text = "Permission required")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                )
            )
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class GpsPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined Location permissions. " + "You can go to the app settings to grant it."
        } else {
            "This app needs access to your Location in order to run correctly!"
        }
    }
}

class AppsUsagePermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you did not accept Applications Usage permissions. " + "You can go to the app settings to grant it."
        } else {
            "This app needs access to your Applications Usage in order to run correctly!"
        }
    }
}

class PhoneCallLogPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined Phone Calls history permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your Phone Calls history in order to run correctly!"
        }
    }
}
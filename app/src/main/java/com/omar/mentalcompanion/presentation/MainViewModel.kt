package com.omar.mentalcompanion.presentation

import android.Manifest
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.mentalcompanion.data.entities.MetaData
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.data.entities.MetaDataValues
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val metaDataRepository: MetaDataRepository
) : ViewModel() {

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CALL_LOG,
    )

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    fun initMetaData() {
        runBlocking {
            if (metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE).isNullOrEmpty()) {
                metaDataRepository.upsertMetaData(
                    MetaData(
                        key = MetaDataKeys.LAST_QUESTIONNAIRE_DATE,
                        value = MetaDataValues.EARLIEST_DATE
                    )
                )

                metaDataRepository.upsertMetaData(
                    MetaData(
                        key = MetaDataKeys.LAST_QUESTIONNAIRE_SCORE,
                        value = MetaDataValues.NO_SCORE
                    )
                )
            }
        }
    }

    fun getStartDestination(): String {
        return runBlocking {
            val lastQuestionnaireDate = async { metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE) }
            val today = LocalDate.now()
            val lastQuestionnaireDateParsed = LocalDate.parse(lastQuestionnaireDate.await())
            val daysSinceLastQuestionnaire = Period.between(lastQuestionnaireDateParsed, today).days

            if (daysSinceLastQuestionnaire >= 7) {
                ActiveScreen.QuestionnaireScreen.getRouteWithArgs("0")
            } else {
                ActiveScreen.CollectedDataScreen.route
            }
        }
    }
}
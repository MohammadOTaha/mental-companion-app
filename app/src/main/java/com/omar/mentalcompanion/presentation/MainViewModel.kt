package com.omar.mentalcompanion.presentation

import android.Manifest
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.omar.mentalcompanion.data.entities.MetaData
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.data.entities.MetaDataValues
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.domain.services.NotificationSchedulerService
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.Period
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val metaDataRepository: MetaDataRepository,
    private val notificationSchedulerService: NotificationSchedulerService,
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

                metaDataRepository.upsertMetaData(
                    MetaData(
                        key = MetaDataKeys.INTRODUCTION_COMPLETED,
                        value = MetaDataValues.FALSE
                    )
                )

                metaDataRepository.upsertMetaData(
                    MetaData(
                        key = MetaDataKeys.LAST_SLEEP_HOURS,
                        value = MetaDataValues.NO_SCORE
                    )
                )
            }
        }
    }

    fun getDestination(): String {
        return runBlocking {
            val introductionCompleted = async { metaDataRepository.getMetaDataValue(MetaDataKeys.INTRODUCTION_COMPLETED) }
            if (introductionCompleted.await() == MetaDataValues.FALSE) {
                return@runBlocking ActiveScreen.IntroductionScreen.route
            }

            val lastQuestionnaireDate = async { metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE) }
            val today = LocalDate.now()
            val lastQuestionnaireDateParsed = LocalDate.parse(lastQuestionnaireDate.await())
            val daysSinceLastQuestionnaire = Period.between(lastQuestionnaireDateParsed, today).days

            if (daysSinceLastQuestionnaire >= 7) {
                return@runBlocking ActiveScreen.QuestionnaireScreen.route
            } else {
                val lastSleepHours = async { metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_SLEEP_HOURS) }
                if (lastSleepHours.await() == MetaDataValues.NO_SCORE &&
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12
                ) {
                    return@runBlocking ActiveScreen.SleepQuestionScreen.route
                }

                return@runBlocking ActiveScreen.WelcomeBackScreen.route
            }
        }
    }

    fun getTimeUntilNextQuestionnaire(): Int {
        return runBlocking {
            val lastQuestionnaireDate = async { metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE) }
            val today = LocalDate.now()
            val lastQuestionnaireDateParsed = LocalDate.parse(lastQuestionnaireDate.await())
            val daysSinceLastQuestionnaire = Period.between(lastQuestionnaireDateParsed, today).days

            7 - daysSinceLastQuestionnaire
        }
    }

    fun scheduleReminderNotification(
        hourOfDay: Int,
        minute: Int,
        title: String,
        content: String
    ) {
        notificationSchedulerService.scheduleReminderNotification(hourOfDay, minute, title, content)
    }
}
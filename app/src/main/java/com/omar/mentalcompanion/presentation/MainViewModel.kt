package com.omar.mentalcompanion.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.omar.mentalcompanion.data.entities.MetaData
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.data.entities.MetaDataValues
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.domain.services.NotificationSchedulerService
import com.omar.mentalcompanion.presentation.screens.ActiveScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val metaDataRepository: MetaDataRepository,
    private val notificationSchedulerService: NotificationSchedulerService,
) : ViewModel() {

    fun initMetaData() {
        runBlocking {
            if (metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE)
                    .isNullOrEmpty()
            ) {
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

                metaDataRepository.upsertMetaData(
                    MetaData(
                        key = MetaDataKeys.LAST_SLEEP_DATE,
                        value = MetaDataValues.EARLIEST_DATE
                    )
                )
            }
        }
    }

    data class MetaData(
        val introductionCompleted: String,
        val lastQuestionnaireDate: String,
        val lastSleepHours: String,
        val lastSleepDate: String
    )

    suspend fun getDestination(): String = withContext(Dispatchers.IO) {
        val metaDataDeferred = async {
            MetaData(
                introductionCompleted = metaDataRepository.getMetaDataValue(MetaDataKeys.INTRODUCTION_COMPLETED)!!,
                lastQuestionnaireDate = metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE)!!,
                lastSleepHours = metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_SLEEP_HOURS)!!,
                lastSleepDate = metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_SLEEP_DATE)!!
            )
        }

        val metaData = metaDataDeferred.await()
        when {
            metaData.introductionCompleted == MetaDataValues.FALSE -> ActiveScreen.IntroductionScreen.route
            ChronoUnit.DAYS.between(LocalDate.parse(metaData.lastQuestionnaireDate), LocalDate.now()).toInt() >= 7 -> ActiveScreen.QuestionnaireScreen.route
            (metaData.lastSleepHours == MetaDataValues.NO_SCORE || metaData.lastSleepDate != LocalDate.now().toString()) && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12 -> ActiveScreen.SleepQuestionScreen.route
            else -> ActiveScreen.WelcomeBackScreen.route
        }
    }

    suspend fun getTimeUntilNextQuestionnaire(): Int = withContext(Dispatchers.IO) {
        val lastQuestionnaireDate = metaDataRepository.getMetaDataValue(MetaDataKeys.LAST_QUESTIONNAIRE_DATE)
        val daysSinceLastQuestionnaire =
            ChronoUnit.DAYS.between(
                LocalDate.parse(lastQuestionnaireDate),
                LocalDate.now()
            ).toInt()

        7 - daysSinceLastQuestionnaire
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
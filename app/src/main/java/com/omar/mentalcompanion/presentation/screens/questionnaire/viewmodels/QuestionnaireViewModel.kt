package com.omar.mentalcompanion.presentation.screens.questionnaire.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.omar.mentalcompanion.data.entities.MetaData
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.presentation.screens.questionnaire.events.QuestionnaireEvent
import com.omar.mentalcompanion.presentation.screens.questionnaire.states.QuestionnaireState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val metaDataRepository: MetaDataRepository
): ViewModel() {
    private val _state = mutableStateOf(QuestionnaireState())
    private val state: State<QuestionnaireState> = _state

    fun getQuestionNumber() = state.value.questionNumber

    fun getScore() = state.value.selectedAnswers.sum()

    fun onEvent(event: QuestionnaireEvent) {
        when (event) {
            is QuestionnaireEvent.NextQuestion -> {
                _state.value = state.value.copy(
                    questionNumber = state.value.questionNumber + 1
                )
            }
            is QuestionnaireEvent.PreviousQuestion -> {
                _state.value = state.value.copy(
                    questionNumber = state.value.questionNumber - 1
                )
            }
            is QuestionnaireEvent.SelectAnswer -> {
                _state.value = state.value.copy(
                    selectedAnswers = state.value.selectedAnswers.apply {
                        set(event.questionNumber, event.answerNumber)
                    }
                )
            }
            is QuestionnaireEvent.FinishQuestionnaire -> {
                runBlocking {
                    metaDataRepository.upsertMetaData(
                        MetaData(
                            key = MetaDataKeys.LAST_QUESTIONNAIRE_DATE,
                            value = LocalDate.now().toString()
                        )
                    )

                    metaDataRepository.upsertMetaData(
                        MetaData(
                            key = MetaDataKeys.LAST_QUESTIONNAIRE_SCORE,
                            value = getScore().toString()
                        )
                    )
                }
            }
            is QuestionnaireEvent.SelectSleepHours -> {
                runBlocking {
                    metaDataRepository.upsertMetaData(
                        MetaData(
                            key = MetaDataKeys.LAST_SLEEP_HOURS,
                            value = event.hours.toString()
                        )
                    )
                }
            }
        }
    }
}
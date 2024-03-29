package com.omar.mentalcompanion.presentation.screens.introduction.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.omar.mentalcompanion.data.entities.MetaData
import com.omar.mentalcompanion.data.entities.MetaDataKeys
import com.omar.mentalcompanion.data.entities.MetaDataValues
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.presentation.screens.introduction.events.IntroductionPageEvent
import com.omar.mentalcompanion.presentation.screens.introduction.states.IntroductionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val metaDataRepository: MetaDataRepository
): ViewModel() {
    private val _state = mutableStateOf(IntroductionState())

    fun getPage() = _state.value.page

    fun getIsRequestPermissionButtonPressed() = _state.value.isRequestPermissionButtonPressed

    fun setIsRequestPermissionButtonPressed(isRequestPermissionButtonPressed: Boolean) {
        _state.value = _state.value.copy(
            isRequestPermissionButtonPressed = isRequestPermissionButtonPressed
        )
    }

    fun onEvent(event: IntroductionPageEvent) {
        when (event) {
            is IntroductionPageEvent.NextPage -> {
                _state.value = _state.value.copy(
                    page = _state.value.page + 1
                )
            }
            is IntroductionPageEvent.PreviousPage -> {
                _state.value = _state.value.copy(
                    page = _state.value.page - 1
                )
            }
            is IntroductionPageEvent.FinishIntroduction -> {
                runBlocking {
                    metaDataRepository.upsertMetaData(
                        MetaData(
                            key = MetaDataKeys.INTRODUCTION_COMPLETED,
                            value = MetaDataValues.TRUE
                        )
                    )
                }
            }
            is IntroductionPageEvent.RequestPermission -> {
                setIsRequestPermissionButtonPressed(true)
            }
        }
    }
}
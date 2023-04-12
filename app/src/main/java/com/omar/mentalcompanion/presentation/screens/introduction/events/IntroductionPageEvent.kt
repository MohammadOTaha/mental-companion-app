package com.omar.mentalcompanion.presentation.screens.introduction.events

sealed class IntroductionPageEvent {
    object NextPage : IntroductionPageEvent()
    object PreviousPage : IntroductionPageEvent()
    object FinishIntroduction : IntroductionPageEvent()
}

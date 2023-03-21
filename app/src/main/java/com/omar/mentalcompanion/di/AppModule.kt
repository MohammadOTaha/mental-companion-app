package com.omar.mentalcompanion.di

import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.AppViewModel
import com.omar.mentalcompanion.data.tracked_data.UsageStatsData
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { UsageStatsData(androidApplication()) }
    viewModel { AppViewModel(androidApplication()) }
    single { FirebaseFirestore.getInstance() }
}
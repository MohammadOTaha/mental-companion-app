package com.omar.mentalcompanion.presentation.screens.collected_data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.mentalcompanion.data.entities.Location
import com.omar.mentalcompanion.domain.repositories.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    fun insertLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLocation(location)
        }
    }

    fun getAllLocations(): List<Location> {
        return repository.getAllLocations()
    }
}
package com.tk.tktask.location.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tk.tktask.location.repository.IMapRepository
import com.tk.tktask.realmdb.enitity.LocationEntity

class LocationViewModel(private val repository: IMapRepository):ViewModel() {
    private val _location = MutableLiveData<List<LocationEntity>>()
    val location: LiveData<List<LocationEntity>> = _location

    init {
        loadLocationHistory()
    }

    private fun loadLocationHistory() {
        val history = repository.getCurrentUserLocationHistory()
        _location.value = history
    }
}
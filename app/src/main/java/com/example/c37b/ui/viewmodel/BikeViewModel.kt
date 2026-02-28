package com.example.c37b.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.c37b.data.Bike
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BikeViewModel(private val repository: BikeRepository) : ViewModel() {

    val allBikes: LiveData<List<Bike>> = repository.getAllBikes().asLiveData()

    fun getBike(id: Int): Flow<Bike?> {
        return repository.getBikeById(id)
    }

    fun addBike(bike: Bike) {
        viewModelScope.launch {
            repository.insertBike(bike)
        }
    }

    fun updateBike(bike: Bike) {
        viewModelScope.launch {
            repository.updateBike(bike)
        }
    }

    fun deleteBike(bike: Bike) {
        viewModelScope.launch {
            repository.deleteBike(bike)
        }
    }
}

class BikeViewModelFactory(private val repository: BikeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BikeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BikeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

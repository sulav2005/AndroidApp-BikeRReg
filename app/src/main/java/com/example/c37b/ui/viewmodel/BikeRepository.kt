package com.example.c37b.ui.viewmodel

import com.example.c37b.data.Bike
import com.example.c37b.data.BikeDao
import kotlinx.coroutines.flow.Flow

class BikeRepository(private val bikeDao: BikeDao) {

    fun getAllBikes(): Flow<List<Bike>> = bikeDao.getAllBikes()

    fun getBikeById(id: Int): Flow<Bike?> = bikeDao.getBikeById(id)

    suspend fun insertBike(bike: Bike) {
        bikeDao.insertBike(bike)
    }

    suspend fun updateBike(bike: Bike) {
        bikeDao.updateBike(bike)
    }

    suspend fun deleteBike(bike: Bike) {
        bikeDao.deleteBike(bike)
    }
}

package com.example.c37b.data.db.dao

import androidx.room.*
import com.example.c37b.data.db.Bike

@Dao
interface BikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBike(bike: Bike)

    @Query("SELECT * FROM bikes")
    suspend fun getAllBikes(): List<Bike>

    @Update
    suspend fun updateBike(bike: Bike)

    @Delete
    suspend fun deleteBike(bike: Bike)
}

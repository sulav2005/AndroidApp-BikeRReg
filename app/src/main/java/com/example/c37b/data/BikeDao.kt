package com.example.c37b.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBike(bike: Bike)

    @Update
    suspend fun updateBike(bike: Bike)

    @Delete
    suspend fun deleteBike(bike: Bike)

    @Query("SELECT * FROM bikes ORDER BY name ASC")
    fun getAllBikes(): Flow<List<Bike>>

    @Query("SELECT * FROM bikes WHERE id = :id")
    fun getBikeById(id: Int): Flow<Bike?>
}

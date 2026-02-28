package com.example.c37b.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bikes")
data class Bike(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lastServiceDate: String,
    val currentMileage: Int
)

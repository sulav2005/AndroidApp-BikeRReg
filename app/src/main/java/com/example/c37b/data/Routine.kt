package com.example.c37b.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectName: String,
    val day: String,
    val startTime: String,
    val endTime: String,
    val room: String = ""
)

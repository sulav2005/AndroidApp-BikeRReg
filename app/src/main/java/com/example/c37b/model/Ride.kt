package com.example.c37b.model

data class Ride(
    var rideId: String = "",
    val title: String = "",
    val destination: String = "",
    val date: String = "",
    val meetupLocation: String = "",
    val bikeType: String = "",
    val bikeNumber: String = "",
    val difficulty: String = "", // Easy, Moderate, Hard
    val maxRiders: Int = 10,
    val joinedUsers: MutableList<String> = mutableListOf(),
    val organizerId: String = "",
    val status: String = "Open", // Open, Full, Completed
    val imageUrl: String = ""
)

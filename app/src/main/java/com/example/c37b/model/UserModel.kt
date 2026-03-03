package com.example.c37b.model

data class UserModel(
    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val phoneNumber: String = "",
    val bikeNumber: String = "",
    val gender : String = "",
    val dob : String = "",
    val email : String = "",
    val password  :String = "",
    val registrationDate: Long = 0L
){
    fun toMap() : Map<String,Any?>{
        return mapOf(
           "id" to id,
           "firstName" to firstName,
           "lastName" to lastName,
           "phoneNumber" to phoneNumber,
           "bikeNumber" to bikeNumber,
           "gender" to gender,
           "dob" to dob,
           "email" to email,
            "password" to password,
            "registrationDate" to registrationDate
        )
    }
}

package com.example.c37b.model

data class UserModel(
    val id : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val phoneNumber: String = "",
    val gender : String = "",
    val dob : String = "",
    val email : String = "",
    val password  :String = ""
){
    fun toMap() : Map<String,Any?>{
        return mapOf(
           "id" to id,
           "firstName" to firstName,
           "lastName" to lastName,
           "phoneNumber" to phoneNumber,
           "gender" to gender,
           "dob" to dob,
           "email" to email,
            "password" to password
        )
    }
}

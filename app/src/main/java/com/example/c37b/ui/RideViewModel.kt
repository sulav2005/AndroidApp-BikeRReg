package com.example.c37b.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.c37b.model.Ride
import com.example.c37b.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class RideViewModel : ViewModel() {

    private val _rides = mutableStateListOf<Ride>()
    val rides: List<Ride> = _rides

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    private val _userDetails = MutableStateFlow<UserModel?>(null)
    val userDetails: StateFlow<UserModel?> = _userDetails


    private val database = FirebaseDatabase.getInstance("https://bikerideregistrationapp-default-rtdb.firebaseio.com/").getReference("rides")
    private val usersDatabase = FirebaseDatabase.getInstance("https://bikerideregistrationapp-default-rtdb.firebaseio.com/").getReference("users")
    private val auth = FirebaseAuth.getInstance()

    init {
        // Sync currentUser with Firebase Auth
        val currentEmail = auth.currentUser?.email
        _currentUser.value = currentEmail
        _userRole.value = if (currentEmail == "admin@gmail.com") "Organizer" else "User"
        
        if (auth.currentUser != null) {
            fetchUserDetails(auth.currentUser?.uid ?: "")
        }

        // Sync Rides from Firebase Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _rides.clear()
                for (rideSnapshot in snapshot.children) {
                    val ride = rideSnapshot.getValue(Ride::class.java)
                    ride?.let {
                        it.rideId = rideSnapshot.key ?: ""
                        _rides.add(it)
                    }
                }
                _rides.forEach { updateRideStatusLocally(it.rideId) }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun fetchUserDetails(userId: String) {
        usersDatabase.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _userDetails.value = snapshot.getValue(UserModel::class.java)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun login(email: String, password: String, onResult: (String?) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            onResult("Please fill all fields")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                _currentUser.value = email
                _userRole.value = if (email == "admin@gmail.com") "Organizer" else "User"
                fetchUserDetails(user?.uid ?: "")
                onResult(null)
            }
            .addOnFailureListener {
                onResult(it.message ?: "Login Failed")
            }
    }

    fun signUp(email: String, password: String, phoneNumber: String, bikeNumber: String, onResult: (String?) -> Unit) {
        if (email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || bikeNumber.isEmpty()) {
            onResult("Please fill all fields")
            return
        }
        if (phoneNumber.length < 10) {
            onResult("Phone number must be at least 10 digits")
            return
        }
        if (password.length < 6) {
            onResult("Password must be at least 6 characters")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid ?: ""
                val user = UserModel(
                    id = userId,
                    email = email,
                    phoneNumber = phoneNumber,
                    bikeNumber = bikeNumber,
                    password = password,
                    registrationDate = System.currentTimeMillis()
                )
                
                usersDatabase.child(userId).setValue(user)
                    .addOnSuccessListener {
                        _currentUser.value = email
                        _userRole.value = if (email == "admin@gmail.com") "Organizer" else "User"
                        _userDetails.value = user
                        onResult(null)
                    }
                    .addOnFailureListener {
                        onResult("Auth success, but failed to save user data: ${it.message}")
                    }
            }
            .addOnFailureListener {
                onResult(it.message ?: "Sign Up Failed")
            }
    }

    fun updateProfile(firstName: String, lastName: String, phoneNumber: String, onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onResult("Not logged in")
        if (phoneNumber.length < 10) return onResult("Phone number must be at least 10 digits")

        val updates = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "phoneNumber" to phoneNumber
        )

        usersDatabase.child(userId).updateChildren(updates)
            .addOnSuccessListener { onResult(null) }
            .addOnFailureListener { onResult(it.message) }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _userRole.value = null
        _userDetails.value = null
    }

    fun addRide(ride: Ride) {
        val newRideRef = database.push()
        val newRide = ride.copy(rideId = newRideRef.key ?: "", organizerId = _currentUser.value ?: "")
        newRideRef.setValue(newRide)
    }

    fun updateRide(updatedRide: Ride) {
        database.child(updatedRide.rideId).setValue(updatedRide)
    }

    fun deleteRide(rideId: String) {
        database.child(rideId).removeValue()
    }

    fun joinRide(rideId: String, userEmail: String): String {
        val ride = _rides.find { it.rideId == rideId } ?: return "Error"
        
        if (ride.joinedUsers.size >= ride.maxRiders) return "Full"
        if (ride.joinedUsers.contains(userEmail)) return "Already Joined"
        
        val updatedUsers = ride.joinedUsers.toMutableList()
        updatedUsers.add(userEmail)
        
        val newStatus = if (updatedUsers.size >= ride.maxRiders) "Full" else ride.status
        
        database.child(rideId).updateChildren(mapOf(
            "joinedUsers" to updatedUsers,
            "status" to newStatus
        ))
        
        return "Success"
    }

    fun leaveRide(rideId: String, userEmail: String) {
        val ride = _rides.find { it.rideId == rideId } ?: return
        val updatedUsers = ride.joinedUsers.toMutableList()
        if (updatedUsers.remove(userEmail)) {
            database.child(rideId).updateChildren(mapOf(
                "joinedUsers" to updatedUsers,
                "status" to "Open"
            ))
        }
    }

    private fun updateRideStatusLocally(rideId: String) {
        val index = _rides.indexOfFirst { it.rideId == rideId }
        if (index != -1) {
            val ride = _rides[index]
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            
            if (ride.date < currentDate && ride.status != "Completed") {
                database.child(rideId).child("status").setValue("Completed")
            }
        }
    }
}

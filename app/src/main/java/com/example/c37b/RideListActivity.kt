package com.example.c37b

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.c37b.adapter.RideAdapter
import com.example.c37b.databinding.ActivityRideListBinding
import com.example.c37b.model.Ride
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RideListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRideListBinding
    private lateinit var rideAdapter: RideAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("rides")

        setupRecyclerView()

        binding.fabAddRide.setOnClickListener {
            // Placeholder: Start AddRideActivity
            Toast.makeText(this, "Add Ride functionality coming soon", Toast.LENGTH_SHORT).show()
        }

        loadRides()
    }

    private fun setupRecyclerView() {
        rideAdapter = RideAdapter(emptyList()) { ride ->
            // Placeholder: Start RideDetailActivity
            Toast.makeText(this, "Clicked on ${ride.title}", Toast.LENGTH_SHORT).show()
        }
        binding.rvRides.layoutManager = LinearLayoutManager(this)
        binding.rvRides.adapter = rideAdapter
    }

    private fun loadRides() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ridesList = mutableListOf<Ride>()
                for (rideSnapshot in snapshot.children) {
                    val ride = rideSnapshot.getValue(Ride::class.java)
                    ride?.let {
                        it.rideId = rideSnapshot.key ?: ""
                        ridesList.add(it)
                    }
                }
                rideAdapter.updateData(ridesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RideListActivity, "Failed to load rides: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

package com.example.c37b.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.c37b.databinding.ItemRideBinding
import com.example.c37b.model.Ride

class RideAdapter(
    private var rides: List<Ride>,
    private val onRideClick: (Ride) -> Unit
) : RecyclerView.Adapter<RideAdapter.RideViewHolder>() {

    class RideViewHolder(val binding: ItemRideBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val binding = ItemRideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        val ride = rides[position]
        holder.binding.apply {
            tvRideTitle.text = ride.title
            tvDestination.text = "To: ${ride.destination}"
            tvDate.text = "Date: ${ride.date}"
            val joined = ride.joinedUsers.size
            tvCapacity.text = "$joined / ${ride.maxRiders} Joined"
            
            root.setOnClickListener { onRideClick(ride) }
        }
    }

    override fun getItemCount() = rides.size

    fun updateData(newRides: List<Ride>) {
        rides = newRides
        notifyDataSetChanged()
    }
}

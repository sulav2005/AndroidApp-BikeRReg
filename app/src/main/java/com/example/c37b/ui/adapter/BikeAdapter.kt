package com.example.c37b.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.c37b.data.db.Bike
import com.example.c37b.databinding.ItemBikeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BikeAdapter(
    private var bikes: List<Bike>,
    private val onEditClick: (Bike) -> Unit,
    private val onDeleteClick: (Bike) -> Unit
) : RecyclerView.Adapter<BikeAdapter.BikeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeViewHolder {
        val binding = ItemBikeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BikeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BikeViewHolder, position: Int) {
        holder.bind(bikes[position])
    }

    override fun getItemCount(): Int = bikes.size

    fun updateBikes(newBikes: List<Bike>) {
        bikes = newBikes
        notifyDataSetChanged()
    }

    inner class BikeViewHolder(private val binding: ItemBikeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bike: Bike) {
            binding.bikeNameTextView.text = bike.name
            binding.lastServiceDateTextView.text = "Last Service: ${bike.lastServiceDate}"
            binding.mileageTextView.text = "Mileage: ${bike.currentMileage} km"

            // Calculate next service
            val nextServiceMileage = (bike.currentMileage / 3000 + 1) * 3000
            val remainingKm = nextServiceMileage - bike.currentMileage

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            try {
                sdf.parse(bike.lastServiceDate)?.let {
                    calendar.time = it
                }
            } catch (e: Exception) {
                // handle date parsing error
            }
            calendar.add(Calendar.MONTH, 6) // Or calculate based on average mileage
            val nextServiceDate = sdf.format(calendar.time)


            binding.nextServiceTextView.text = "Next Service: in $remainingKm km or by $nextServiceDate"

            binding.editButton.setOnClickListener { onEditClick(bike) }
            binding.deleteButton.setOnClickListener { onDeleteClick(bike) }
        }
    }
}

package com.rupinder.travelbook.adapters

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.rupinder.travelbook.databinding.AddTourPlacesBinding
import com.rupinder.travelbook.databinding.LayoutTourPlacesItemBinding
import com.rupinder.travelbook.models.TourPlacesEntity
import com.rupinder.travelbook.tourPlacesClickInterface


class TourPlacesRecyclerViewAdapter(
    private val values: List<TourPlacesEntity>,val editClickListner: tourPlacesClickInterface
) : RecyclerView.Adapter<TourPlacesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutTourPlacesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tvTourPlaceName.setText(item.placename)
        holder.tvAddress.setText(item.address)
        holder.tvCharges.setText("₹"+item.dailycharges)
        holder.tvTourPlaceDate.setText(item.tourDate)
        holder.tvTourPlaceTime.setText(item.tourTime)

        if(values[position].Status.equals("Traveled")){
            holder.tvStatus.setText(values[position].Status)
        }
        else{
            //holder.tvStatus.setBackgroundResource(R.drawable.not_status_round)
            holder.tvStatus.setTextColor(Color.RED)
            holder.tvStatus.setText(values[position].Status)
        }

        holder.btnEdit.setOnClickListener {
            editClickListner.updateTourPlace(item)
        }

        holder.btnDelete.setOnClickListener {
            editClickListner.deleteTourPlace(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: LayoutTourPlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTourPlaceName: TextView = binding.tvTourPlaceName
        val tvCharges: TextView = binding.tvTourCharges
        val tvAddress: TextView = binding.tvAddress
        val tvTourPlaceDate: TextView = binding.tvTourPlaceDate
        val tvTourPlaceTime: TextView = binding.tvTourPlaceTime
        val btnEdit: Button = binding.btnEdit
        val btnDelete: Button = binding.btnDelete
        val tvStatus: TextView = binding.tvStatus


    }

}
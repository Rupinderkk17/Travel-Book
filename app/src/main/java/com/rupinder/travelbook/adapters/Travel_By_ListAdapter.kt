package com.rupinder.travelbook.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rupinder.travelbook.*
import com.rupinder.travelbook.models.HotelEntity
import com.rupinder.travelbook.models.FoodEntity
import com.rupinder.travelbook.models.TravelByEntity


class Travel_By_ListAdapter(var travelByEntity:  ArrayList<TravelByEntity>, var recyclerClick: travelbyClickInterface): RecyclerView.Adapter<Travel_By_ListAdapter.ViewHolder>() {
    lateinit var mainActivity: MainActivity
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tvTravelBY = view.findViewById<TextView>(R.id.tvTravelBY)
        var tvTravelByDate = view.findViewById<TextView>(R.id.tvTravelByDate)
        var tvTravelByTime = view.findViewById<TextView>(R.id.tvTravelByTime)
        var tvTravelByCharges = view.findViewById<TextView>(R.id.tvTravelByCharges)
        var btnEdit = view.findViewById<TextView>(R.id.btnEdit)
        var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.travelby_item_list_design, parent, false)
        mainActivity= MainActivity()
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTravelBY.setText(travelByEntity[position].travelBy)
        holder.tvTravelByDate.setText(travelByEntity[position].Date)
        holder.tvTravelByTime.setText(travelByEntity[position].Time)
        holder.tvTravelByCharges.setText("₹"+travelByEntity[position].expense)


        holder.btnEdit.setOnClickListener {
            recyclerClick.editTravelByDetail(travelByEntity[position])
        }

        holder.btnDelete.setOnClickListener {
            recyclerClick.deleteTravelByDetail(travelByEntity[position])

        }
    }

    override fun getItemCount(): Int {
        return travelByEntity.size
    }
}



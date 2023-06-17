package com.rupinder.travelbook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rupinder.travelbook.R
import com.rupinder.travelbook.RecyclerClick
import com.rupinder.travelbook.models.TravelDataEntity


class TravelDataAdapter(var TravelDataEntity: ArrayList<TravelDataEntity>, var recyclerClick: RecyclerClick): RecyclerView.Adapter<TravelDataAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
       var place = view.findViewById<TextView>(R.id.tvPlace)
        var StartDate = view.findViewById<TextView>(R.id.tvStartDATE)
       var EndDate = view.findViewById<TextView>(R.id.tvEndDate)
       var btnDelete = view.findViewById<Button>(R.id.btnDelete)
       var btnEdit = view.findViewById<Button>(R.id.btnEdit)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
   ): ViewHolder {
              var view = LayoutInflater.from(parent.context)
          .inflate(R.layout.layout_travel_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.place.setText(TravelDataEntity[position].PLACE)
        holder.StartDate.setText(TravelDataEntity[position].STARTDATE)
        holder.EndDate.setText(TravelDataEntity[position].ENDDATE)

        holder.view.setOnClickListener {
            recyclerClick.TravelDataClicked(TravelDataEntity[position])
        }
        holder.btnDelete.setOnClickListener {
            recyclerClick.TravelDataDeleteClicked(TravelDataEntity[position])
        }
        holder.btnEdit.setOnClickListener {
            recyclerClick.UpdateTravelData(TravelDataEntity[position])
        }
    }

    override fun getItemCount(): Int {
        return TravelDataEntity.size
    }
}


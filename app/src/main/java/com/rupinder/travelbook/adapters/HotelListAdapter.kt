package com.rupinder.travelbook.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rupinder.travelbook.HotelClickInterface
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.models.HotelEntity
import com.rupinder.travelbook.R


class HotelListAdapter(var hotelEntityList: ArrayList<HotelEntity>, var recyclerClick: HotelClickInterface): RecyclerView.Adapter<HotelListAdapter.ViewHolder>() {
     lateinit var mainActivity: MainActivity
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
       var tvHotel = view.findViewById<TextView>(R.id.tvHotelName)
       var tvHotelAdress = view.findViewById<TextView>(R.id.tvHotelAddress)
       var tvStartDate = view.findViewById<TextView>(R.id.tvStartDATE)
       var tvEndDate = view.findViewById<TextView>(R.id.tvEndDate)
       var tvCharges = view.findViewById<TextView>(R.id.tvHotelCharges)
       var btnEdit = view.findViewById<TextView>(R.id.btnEdit)
       var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
   ): ViewHolder {
              val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.layout_hotel_list_item, parent, false)
        mainActivity= MainActivity()
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvHotel.setText(hotelEntityList[position].hotelName)
        holder.tvHotelAdress.setText("Hotel Address: "+hotelEntityList[position].address)
        holder.tvStartDate.setText(hotelEntityList[position].startDate)
        holder.tvEndDate.setText(hotelEntityList[position].endDate)
        holder.tvCharges.setText("₹ "+hotelEntityList[position].charges)

        holder.btnEdit.setOnClickListener {
            recyclerClick.HotelClicked(hotelEntityList[position])
        }
        holder.btnDelete.setOnClickListener {
            recyclerClick.deleteItem(hotelEntityList[position])
        }
    }

    override fun getItemCount(): Int {
        return hotelEntityList.size
    }
}



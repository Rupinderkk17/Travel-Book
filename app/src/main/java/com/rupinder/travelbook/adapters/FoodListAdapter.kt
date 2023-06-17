package com.rupinder.travelbook.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rupinder.travelbook.FoodclickInterface
import com.rupinder.travelbook.HotelClickInterface
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.models.HotelEntity
import com.rupinder.travelbook.R
import com.rupinder.travelbook.models.FoodEntity


class FoodListAdapter(var foodEntity: ArrayList<FoodEntity>, var recyclerClick: FoodclickInterface): RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {
     lateinit var mainActivity: MainActivity
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
       var tvFoodPlace = view.findViewById<TextView>(R.id.tvFoodPlace)
       var tvfoodDish = view.findViewById<TextView>(R.id.tvfoodDish)
       var tvFoodDetails = view.findViewById<TextView>(R.id.tvFoodDetails)
       var tvFoodDate = view.findViewById<TextView>(R.id.tvFoodDate)
       var tvFoodTime = view.findViewById<TextView>(R.id.tvFoodTime)
       var tvFoodCharges = view.findViewById<TextView>(R.id.tvFoodCharges)
       var btnEdit = view.findViewById<TextView>(R.id.btnEdit)
       var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
   ): ViewHolder {
              val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.food_item_list_design, parent, false)
        mainActivity= MainActivity()
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvFoodPlace.setText(foodEntity[position].foodPlace)
        holder.tvfoodDish.setText(foodEntity[position].foodName)
        holder.tvFoodDetails.setText(foodEntity[position].foodDesc)
        holder.tvFoodDate.setText(foodEntity[position].foodDate)
        holder.tvFoodTime.setText(foodEntity[position].foodTime)
        holder.tvFoodCharges.setText("₹ "+foodEntity[position].foodCharges)


        holder.btnEdit.setOnClickListener {
            recyclerClick.updateFoodDetails(foodEntity[position])
        }

        holder.btnDelete.setOnClickListener {
            recyclerClick.deleteFoodItem(foodEntity[position])
        }
    }

    override fun getItemCount(): Int {
        return foodEntity.size
    }
}



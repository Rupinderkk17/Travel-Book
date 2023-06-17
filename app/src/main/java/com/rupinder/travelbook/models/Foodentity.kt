package com.rupinder.travelbook.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FoodEntity() {

    @PrimaryKey(autoGenerate = true)
    var Id: Int= 0
    @ColumnInfo(name="travel_Id") var travel_Id: Int=0
    @ColumnInfo(name="foodPlace") var foodPlace: String?=null
    @ColumnInfo(name="Food name") var foodName: String?=null
    @ColumnInfo(name="Description") var foodDesc: String? = null
    @ColumnInfo(name="Charges") var foodCharges: String? = null
    @ColumnInfo(name="Date") var foodDate: String? = null
    @ColumnInfo(name="Time") var foodTime: String? = null
}

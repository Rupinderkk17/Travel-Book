package com.rupinder.travelbook.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TourPlacesEntity{

    @PrimaryKey(autoGenerate = true)
    var Id: Int= 0
    @ColumnInfo(name="travel_Id") var travel_Id: Int=0
    @ColumnInfo(name="Status") var Status: String?=null
    @ColumnInfo(name="tour_place") var placename: String?=null
    @ColumnInfo(name="address") var address: String? = null
    @ColumnInfo(name="dailycharges") var dailycharges: String? = null
    @ColumnInfo(name="Date") var tourDate: String? = null
    @ColumnInfo(name="Time") var tourTime: String? = null
}

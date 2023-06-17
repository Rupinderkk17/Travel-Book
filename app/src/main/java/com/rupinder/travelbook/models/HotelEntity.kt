package com.rupinder.travelbook.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class HotelEntity() {

    @PrimaryKey (autoGenerate = true)
    var id: Int= 0

    @ColumnInfo(name="travel_id")
    var travel_id: Int=0
    @ColumnInfo(name="Hotel_Name") var hotelName: String?=null
    @ColumnInfo(name="Address") var address: String? = null
    @ColumnInfo(name="DailyCharges") var charges: String? = null
    @ColumnInfo(name="START DATE") var startDate: String?=null
    @ColumnInfo(name="END DATE") var endDate: String?=null
}


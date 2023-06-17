package com.rupinder.travelbook.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TravelByEntity {

    @PrimaryKey(autoGenerate = true)
    var Id: Int= 0
    @ColumnInfo(name="Travel_id") var travel_Id: Int=0
    @ColumnInfo(name="Travel_By") var travelBy: String?=null
    @ColumnInfo(name="expense") var expense: String? = null
    @ColumnInfo(name="Date") var Date: String? = null
    @ColumnInfo(name="Time") var Time: String? = null
}

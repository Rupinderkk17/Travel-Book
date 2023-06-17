package com.rupinder.travelbook.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TravelDataEntity() {

    @PrimaryKey (autoGenerate = true)
    var Id: Int= 0
    @ColumnInfo(name="PLACE") var PLACE: String?=null
    @ColumnInfo(name="Status") var Status: String?=null
    @ColumnInfo(name="START DATE") var STARTDATE: String?=null
    @ColumnInfo(name="END DATE") var ENDDATE: String?=null
    @ColumnInfo(name="IS COMPLETED") var ISCOMPLETED: Boolean = false
}




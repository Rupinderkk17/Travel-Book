package com.rupinder.travelbook.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rupinder.travelbook.roomdb.Converters

@Entity
@TypeConverters(Converters::class)
class MemoriesEntity() {
    @PrimaryKey(autoGenerate = true)
    var Id: Int= 0
    @ColumnInfo(name="travel_Id") var travel_Id: Int=0
    @ColumnInfo(name="memories") var memories: String?=null
}

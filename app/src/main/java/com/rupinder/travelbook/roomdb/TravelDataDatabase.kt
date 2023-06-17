package com.rupinder.travelbook.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rupinder.travelbook.models.*

@Database(entities = arrayOf(TravelDataEntity::class, HotelEntity::class, FoodEntity::class, TravelByEntity::class, TourPlacesEntity::class, MemoriesEntity::class), version = 3)
 abstract class TravelDataDatabase: RoomDatabase(){
    abstract fun travelDataDao(): TravelDataDao
        companion object {
            var travelDataDatabase: TravelDataDatabase? = null

            @Synchronized
            fun getDatabase(context: Context): TravelDataDatabase {
                if (travelDataDatabase == null) {
                    travelDataDatabase = Room.databaseBuilder(
                        context,
                        TravelDataDatabase::class.java, "Travel"
                    ).build()
                }
                return travelDataDatabase!!
            }
        }
    }
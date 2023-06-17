package com.rupinder.travelbook.roomdb

import androidx.room.*
import com.rupinder.travelbook.models.*

@Dao
interface TravelDataDao {
    @Insert
    fun insetdata(vararg TravelDataEntity: TravelDataEntity)

    @Query("select * from TravelDataEntity")
    fun getTravelData(): List<TravelDataEntity>

    @Update
    fun updateTravelData(vararg travelDataEntity: TravelDataEntity)

    @Update
    fun updateTourData(vararg travelDataEntity: TourPlacesEntity)

    @Delete
    fun delete(vararg TravelDataEntity: TravelDataEntity)

    @Delete
    fun deleteFood(vararg foodEntity: FoodEntity)

    @Delete
    fun deleteTravelBy(vararg travelByEntity: TravelByEntity)

    @Delete
    fun deleteTour(vararg travelByEntity: TourPlacesEntity)

    @Insert
    fun insertHotel(vararg hotelEntity: HotelEntity)

    @Query("select * from HotelEntity where travel_id=:tId")
    fun getHotels(tId:Int): List<HotelEntity>

    @Query("select * from FoodEntity where travel_id=:tId")
    fun getFoodList(tId:Int): List<FoodEntity>

    @Query("select * from TravelByEntity where travel_id=:tId")
    fun getTravelBy(tId:Int): List<TravelByEntity>

    @Update()
    fun updateHotelData(hotelEntity: HotelEntity)

    @Update()
    fun updateFoodData(foodEntity: FoodEntity)

    @Update()
    fun updateFoodData(travelByEntity: TravelByEntity)

    @Delete
    fun deleteHotel(vararg hotelEntity: HotelEntity)

    @Insert
    fun insertFoodDetails(vararg foodEntity: FoodEntity)
    @Insert
    fun addTravelByDetails(vararg travelByEntity: TravelByEntity)
//    abstract fun addTourplaceDetails(tourplacesEntity: tourplacesEntity)

    @Query("select * from TOURPLACESENTITY where travel_id=:tId")
    fun getTourPlace(tId:Int): List<TourPlacesEntity>

    @Delete
    fun deleteTourPlaceDetail(vararg tourplacesEntity: TourPlacesEntity)

    @Insert
    fun addTourplaceDetails(vararg tourplacesEntity: TourPlacesEntity)

    @Update()
    fun editTourPlaceDetail(tourplacesEntity: TourPlacesEntity)

    @Query("select * from MemoriesEntity where travel_id=:tId")
    fun getMemoriesEntity(tId:Int): List<MemoriesEntity>


    @Insert
    fun addMemories(vararg memoriesEntity: MemoriesEntity)

    @Delete
    fun deleteMemories(vararg memoriesEntity: MemoriesEntity)


}
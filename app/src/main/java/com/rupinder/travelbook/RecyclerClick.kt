package com.rupinder.travelbook

import com.rupinder.travelbook.models.*

interface RecyclerClick {
    fun TravelDataClicked (travelDataEntity: TravelDataEntity)
    fun TravelDataDeleteClicked (travelDataEntity: TravelDataEntity)
    fun UpdateTravelData(travelDataEntity: TravelDataEntity)
}
interface HotelClickInterface {
    fun HotelClicked (hotelEntity: HotelEntity)
    fun deleteItem(hotelEntity: HotelEntity)
}
interface FoodclickInterface {
    fun deleteFoodItem(foodEntity: FoodEntity)
    fun updateFoodDetails(foodEntity: FoodEntity)
}
interface travelbyClickInterface {
    fun editTravelByDetail (travelByEntity: TravelByEntity)
    fun deleteTravelByDetail (travelByEntity: TravelByEntity)
}

interface tourPlacesClickInterface {
    fun  deleteTourPlace ( tourPlacesEntity: TourPlacesEntity)
    fun  updateTourPlace ( tourPlacesEntity: TourPlacesEntity)
}

interface memoriesClickInterface {
    fun  deleteMemory ( memoriesEntity: MemoriesEntity)
    fun  viewMemory ( memoriesEntity: MemoriesEntity)
}
package com.rupinder.travelbook.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.type.DateTime
import com.rupinder.travelbook.FoodclickInterface
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.adapters.FoodListAdapter
import com.rupinder.travelbook.databinding.FooditemsBinding
import com.rupinder.travelbook.databinding.FragmentFoodBinding
import com.rupinder.travelbook.models.FoodEntity
import com.rupinder.travelbook.models.TravelDataEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class FoodFragment : Fragment(), FoodclickInterface {
    lateinit var binding: FragmentFoodBinding
    lateinit var mainActivity: MainActivity
    lateinit var foodEntity: FoodEntity
    lateinit var adapter: FoodListAdapter
    lateinit var arrayList: ArrayList<FoodEntity>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        arrayList = ArrayList()
        foodEntity = FoodEntity()

        adapter = FoodListAdapter(arrayList, this)
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = adapter

        getFoodList()
        val dialogBinding = FooditemsBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        binding.fabButton.setOnClickListener {
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )


            dialogBinding.tvDate.setOnClickListener {
                val datePicker = DatePickerDialog(
                    requireContext(),
                    { view, year, month, date ->
                        val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DATE, date)
                        dialogBinding.tvDate.setText(simpleDateFormat.format(selectedDate.time))
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePicker.show()

            }

            dialogBinding.tvTime.setOnClickListener {
                val mTimePicker: TimePickerDialog
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)

                mTimePicker =
                    TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            dialogBinding.tvTime.setText(
                                String.format(
                                    "%d : %d",
                                    hourOfDay,
                                    minute
                                )
                            )
                        }
                    }, hour, minute, false)
                mTimePicker.show()
            }

            dialogBinding.btnAddHotel.setOnClickListener {

                foodEntity.travel_Id = mainActivity.travelDataEntity.Id
                foodEntity.foodPlace = dialogBinding.etFoodPlace.text.toString()
                foodEntity.foodName = dialogBinding.etFoodName.text.toString()
                foodEntity.foodDesc = dialogBinding.etFoodDetails.text.toString()
                foodEntity.foodCharges = dialogBinding.etFoodCharges.text.toString()
                foodEntity.foodDate = dialogBinding.tvDate.text.toString()
                foodEntity.foodTime = dialogBinding.tvTime.text.toString()
//                if (dialogBinding.checkbox.isChecked){
//                    foodEntity.status = "Traveled"
//                }
//                else{
//                    foodEntity.status = "Not Traveled"
//                }

                addFoodDetails(foodEntity)

                dialog.dismiss()
            }
            dialog.show()
        }
        return binding.root
    }

    fun addFoodDetails(foodEntity: FoodEntity) {
        class HotelInfo : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .insertFoodDetails(foodEntity)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                getFoodList()
                Toast.makeText(requireContext(), "Food Details Added", Toast.LENGTH_SHORT).show()
            }
        }
        HotelInfo().execute()
    }


    fun getFoodList() {
        class GetHotels : AsyncTask<Void, Void, Void>() {
            val travel_id = mainActivity.travelDataEntity.Id
            override fun doInBackground(vararg params: Void?): Void? {

                id.let {
                    arrayList.addAll(
                        TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                            .getFoodList(travel_id)
                    )
                }
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                if (arrayList.size == 0) {
                    binding.layoutNodata.visibility = View.VISIBLE
                    binding.listView.visibility = View.GONE
                } else {
                    binding.layoutNodata.visibility = View.GONE
                    binding.listView.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                }


            }

        }
        GetHotels().execute()
    }

    override fun deleteFoodItem(foodEntity: FoodEntity) {
        delPopUp(foodEntity)
    }

    fun delPopUp(foodEntity: FoodEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Food Item")
        alertDialog.setMessage("Do you want to delete this Food Details ?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            deletedata(foodEntity)
        }
        alertDialog.setNegativeButton("No") { _, _ -> }
        alertDialog.show()
    }

    private fun deletedata(foodEntity: FoodEntity) {
        class deleteN : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .deleteFood(foodEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), "Food History Delete", Toast.LENGTH_SHORT).show()
                arrayList.clear()
                getFoodList()
            }
        }

        deleteN().execute()

    }

    override fun updateFoodDetails(foodEntity: FoodEntity) {
        updatePopUp(foodEntity)
    }

    fun updateFood(foodEntity: FoodEntity) {
        class HotelInfo : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .updateFoodData(foodEntity)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                arrayList.clear()
                getFoodList()
                Toast.makeText(requireContext(), "Food Details Updated", Toast.LENGTH_SHORT).show()
            }
        }
        HotelInfo().execute()
    }


    fun updatePopUp(foodEntity: FoodEntity) {
        val dialogBinding = FooditemsBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )

        dialogBinding.etFoodPlace.setText(foodEntity.foodPlace)
        dialogBinding.etFoodName.setText(foodEntity.foodName)
        dialogBinding.etFoodDetails.setText(foodEntity.foodDesc)
        dialogBinding.etFoodCharges.setText(foodEntity.foodCharges)
        dialogBinding.tvDate.setText(foodEntity.foodDate)
        dialogBinding.tvTime.setText(foodEntity.foodTime)


        dialogBinding.btnAddHotel.setText("Update Food Details")
        dialog.setCancelable(false)
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.tvDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    dialogBinding.tvDate.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()

        }

        dialogBinding.tvTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker =
                TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        dialogBinding.tvTime.setText(
                            String.format(
                                "%d : %d",
                                hourOfDay,
                                minute
                            )
                        )
                    }
                }, hour, minute, false)
            mTimePicker.show()
        }

        dialogBinding.btnAddHotel.setOnClickListener {
            foodEntity.travel_Id = mainActivity.travelDataEntity.Id
            foodEntity.foodPlace = dialogBinding.etFoodPlace.text.toString()
            foodEntity.foodName = dialogBinding.etFoodName.text.toString()
            foodEntity.foodDesc = dialogBinding.etFoodDetails.text.toString()
            foodEntity.foodCharges = dialogBinding.etFoodCharges.text.toString()
            foodEntity.foodDate = dialogBinding.tvDate.text.toString()
            foodEntity.foodTime = dialogBinding.tvTime.text.toString()
            updateFood(foodEntity)
            dialog.dismiss()
        }
        dialog.show()

    }

}
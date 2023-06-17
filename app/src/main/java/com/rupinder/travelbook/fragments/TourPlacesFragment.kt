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
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.adapters.TourPlacesRecyclerViewAdapter
import com.rupinder.travelbook.databinding.AddTourPlacesBinding
import com.rupinder.travelbook.databinding.FragmentTourPlacesBinding
import com.rupinder.travelbook.databinding.UpdateTourPlacesBinding
import com.rupinder.travelbook.models.TourPlacesEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase
import com.rupinder.travelbook.tourPlacesClickInterface
import java.text.SimpleDateFormat
import java.util.*

class TourPlacesFragment : Fragment(), tourPlacesClickInterface {
    lateinit var binding: FragmentTourPlacesBinding
    lateinit var mainActivity: MainActivity
    var arrayList: ArrayList<TourPlacesEntity> = ArrayList()
    lateinit var tourPlacesEntity: TourPlacesEntity
    lateinit var adapter: TourPlacesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourPlacesBinding.inflate(inflater, container, false)
        tourPlacesEntity = TourPlacesEntity()
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TourPlacesRecyclerViewAdapter(arrayList,this)
        binding.listView.adapter = adapter
        getTourPlacesList()
//      add tour places
        val dialogBinding = AddTourPlacesBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        binding.fabButton.setOnClickListener {
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )

            dialogBinding.btnClose.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.tvTourPlaceDate.setOnClickListener {
                val datePicker = DatePickerDialog(
                    requireContext(),
                    { view, year, month, date ->
                        val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DATE, date)
                        dialogBinding.tvTourPlaceDate.setText(simpleDateFormat.format(selectedDate.time))
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePicker.show()

            }

            dialogBinding.tvTourPlaceTime.setOnClickListener {
                val mTimePicker: TimePickerDialog
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)

                mTimePicker =
                    TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            dialogBinding.tvTourPlaceTime.setText(
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

            dialogBinding.btnAddTourPlace.setOnClickListener {
                tourPlacesEntity.travel_Id = mainActivity.travelDataEntity.Id
                tourPlacesEntity.placename = dialogBinding.etTourPlace.text.toString()
                tourPlacesEntity.address = dialogBinding.etTourAddress.text.toString()
                tourPlacesEntity.dailycharges = dialogBinding.etTourCharges.text.toString()
                tourPlacesEntity.tourDate = dialogBinding.tvTourPlaceDate.text.toString()
                tourPlacesEntity.tourTime = dialogBinding.tvTourPlaceTime.text.toString()

                if (dialogBinding.checkbox.isChecked){
                    tourPlacesEntity.Status = "Traveled"
                }
                else{
                    tourPlacesEntity.Status = "Not Traveled"
                }

                addTourPlace(tourPlacesEntity)

                dialog.dismiss()
            }
            dialog.show()
        }
//        end tour places added code
        return binding.root
    }

    override fun updateTourPlace(tourPlacesEntity: TourPlacesEntity) {
        updateTPlace(tourPlacesEntity)
    }

    override fun deleteTourPlace(tourPlacesEntity: TourPlacesEntity) {
        delPopUp(tourPlacesEntity)
    }

    fun addTourPlace(tourPlacesEntity: TourPlacesEntity) {
        class TourInfo : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .addTourplaceDetails(tourPlacesEntity)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                arrayList.clear()
                getTourPlacesList()
                Toast.makeText(requireContext(), "Tour Place Added!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        TourInfo().execute()
    }


    fun getTourPlacesList() {
        class GetHotels : AsyncTask<Void, Void, Void>() {
            val travel_id = mainActivity.travelDataEntity.Id
            override fun doInBackground(vararg params: Void?): Void? {

                id.let {
                    arrayList.addAll(
                        TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                            .getTourPlace(travel_id)
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

    fun updateTPlace(tourPlacesEntity: TourPlacesEntity) {

        val dialogBinding = UpdateTourPlacesBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )

        dialogBinding.etTourPlace.setText(tourPlacesEntity.placename)
        dialogBinding.etTourAddress.setText(tourPlacesEntity.address)
        dialogBinding.etTourCharges.setText(tourPlacesEntity.dailycharges)
        dialogBinding.tvTourPlaceDate.setText(tourPlacesEntity.tourDate)
        dialogBinding.tvTourPlaceTime.setText(tourPlacesEntity.tourTime)

        dialogBinding.tvTourPlaceDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    dialogBinding.tvTourPlaceDate.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()

        }

        dialogBinding.tvTourPlaceTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker =
                TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        dialogBinding.tvTourPlaceTime.setText(
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


        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnAddTourPlace.setOnClickListener {
            class updateHotelInfo : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    tourPlacesEntity.travel_Id = mainActivity.travelDataEntity.Id
                    tourPlacesEntity.placename = dialogBinding.etTourPlace.text.toString()
                    tourPlacesEntity.address = dialogBinding.etTourAddress.text.toString()
                    tourPlacesEntity.dailycharges = dialogBinding.etTourCharges.text.toString()
                    tourPlacesEntity.tourDate = dialogBinding.tvTourPlaceDate.text.toString()
                    tourPlacesEntity.tourTime = dialogBinding.tvTourPlaceTime.text.toString()

                    id.let {
                        TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                            .updateTourData(tourPlacesEntity)
                    }
                    return null
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    arrayList.clear()
                    getTourPlacesList()
                    Toast.makeText(requireContext(), "Tour Places Updated", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            updateHotelInfo().execute()
            dialog.dismiss()
        }

        dialog.show()
    }


    fun delPopUp(tourPlacesEntity: TourPlacesEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Traveling Details")
        alertDialog.setMessage("Do you want to delete this TravelBy Details ?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            deletedata(tourPlacesEntity)
        }
        alertDialog.setNegativeButton("No") { _, _ -> }
        alertDialog.show()
    }

    private fun deletedata(tourPlacesEntity: TourPlacesEntity) {
        class deleteN : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .deleteTour(tourPlacesEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), "Tour Place History Deleted!", Toast.LENGTH_SHORT)
                    .show()
                arrayList.clear()
                getTourPlacesList()
            }
        }

        deleteN().execute()

    }

}
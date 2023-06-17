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
import com.rupinder.travelbook.adapters.Travel_By_ListAdapter
import com.rupinder.travelbook.databinding.AddTravelByInfoBinding
import com.rupinder.travelbook.databinding.FragmentTravelByBinding
import com.rupinder.travelbook.models.TravelByEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase
import com.rupinder.travelbook.travelbyClickInterface
import java.text.SimpleDateFormat
import java.util.*

class TravelByFragment : Fragment(), travelbyClickInterface {
    lateinit var binding: FragmentTravelByBinding
    lateinit var mainActivity: MainActivity
    var travelByEntity: TravelByEntity= TravelByEntity()
    lateinit var adapter: Travel_By_ListAdapter
     var arrayList: ArrayList<TravelByEntity> =  ArrayList()


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
        binding = FragmentTravelByBinding.inflate(inflater, container, false)
        adapter = Travel_By_ListAdapter(arrayList, this)
        binding.listView.layoutManager = LinearLayoutManager(mainActivity)
        binding.listView.adapter = adapter
        getTravelByList()

        binding.fabButton.setOnClickListener {
            val dialogBinding = AddTravelByInfoBinding.inflate(layoutInflater)
            val dialog = Dialog(requireContext())
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )

            dialogBinding.tvTravelByDate.setOnClickListener {
                val datePicker = DatePickerDialog(
                    requireContext(),
                    { view, year, month, date ->
                        val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DATE, date)
                        dialogBinding.tvTravelByDate.setText(simpleDateFormat.format(selectedDate.time))
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePicker.show()

            }

            dialogBinding.tvTravelByTime.setOnClickListener {
                val mTimePicker: TimePickerDialog
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
                val minute = mcurrentTime.get(Calendar.MINUTE)

                mTimePicker =
                    TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                            dialogBinding.tvTravelByTime.setText(
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
                travelByEntity.travel_Id = mainActivity.travelDataEntity.Id
                travelByEntity.Date = dialogBinding.tvTravelByDate.text.toString()
                travelByEntity.Time = dialogBinding.tvTravelByTime.text.toString()
                travelByEntity.travelBy = dialogBinding.etTravelBySource.text.toString()
                travelByEntity.expense = dialogBinding.etTravelByCharges.text.toString()
                addTravelByDetails(travelByEntity)

                dialog.dismiss()
            }
            dialog.show()
        }
        return binding.root
    }

    fun addTravelByDetails(travelByEntity: TravelByEntity) {
        class HotelInfo : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .addTravelByDetails(travelByEntity)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                arrayList.clear()
                getTravelByList()
                Toast.makeText(requireContext(), "TravelBy Details Added", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        HotelInfo().execute()
    }


    fun getTravelByList() {
        class GetHotels : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                mainActivity.travelDataEntity.Id.let {
                    arrayList.addAll(
                        TravelDataDatabase.getDatabase(mainActivity).travelDataDao().getTravelBy(it)
                    )
                }
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                if (arrayList.isEmpty()) {
                    binding.layout.visibility = View.VISIBLE
                    binding.listView.visibility = View.GONE
                } else {
                    binding.layout.visibility = View.GONE
                    binding.listView.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                }


            }

        }
        GetHotels().execute()
    }


    fun delPopUp(travelByEntity: TravelByEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Traveling Details")
        alertDialog.setMessage("Do you want to delete this TravelBy Details ?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            deletedata(travelByEntity)
        }
        alertDialog.setNegativeButton("No") { _, _ -> }
        alertDialog.show()
    }

    private fun deletedata(travelByEntity: TravelByEntity) {
        class deleteN : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .deleteTravelBy(travelByEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), "TravelBy History Delete", Toast.LENGTH_SHORT)
                    .show()
                arrayList.clear()
                getTravelByList()
            }
        }

        deleteN().execute()

    }

    fun updateFood(travelByEntity: TravelByEntity) {
        class HotelInfo : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .updateFoodData(travelByEntity)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                arrayList.clear()
                getTravelByList()
                Toast.makeText(requireContext(), "TravelBY Details Updated", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        HotelInfo().execute()
    }

    override fun editTravelByDetail(travelByEntity: TravelByEntity) {
     updatePopUp(travelByEntity)
    }

    override fun deleteTravelByDetail(travelByEntity: TravelByEntity) {
        delPopUp(travelByEntity)
    }


    fun updatePopUp(travelByEntity: TravelByEntity) {
        val dialogBinding = AddTravelByInfoBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        dialogBinding.etTravelBySource.setText(travelByEntity.travelBy)
        dialogBinding.etTravelByCharges.setText(travelByEntity.expense)

        dialogBinding.btnAddHotel.setText("Update Travel By Details")
        dialog.setCancelable(false)
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.tvTravelByDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    dialogBinding.tvTravelByDate.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()

        }

        dialogBinding.tvTravelByTime.setOnClickListener {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            mTimePicker =
                TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        dialogBinding.tvTravelByTime.setText(
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

            updateFood(travelByEntity)
            dialog.dismiss()
        }
        dialog.show()

    }

}
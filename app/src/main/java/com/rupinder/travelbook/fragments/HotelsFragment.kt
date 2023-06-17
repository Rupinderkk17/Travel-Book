package com.rupinder.travelbook.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Update
import com.rupinder.travelbook.HotelClickInterface
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.R
import com.rupinder.travelbook.adapters.HotelListAdapter
import com.rupinder.travelbook.databinding.FragmentHotelsBinding
import com.rupinder.travelbook.databinding.FragmentHotelsBinding.inflate
import com.rupinder.travelbook.databinding.HotelsenttryBinding
import com.rupinder.travelbook.databinding.HotelsenttryupdateBinding
import com.rupinder.travelbook.models.HotelEntity
import com.rupinder.travelbook.models.TravelDataEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase
import com.rupinder.travelbook.roomdb.TravelDataDatabase.Companion.getDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HotelsFragment : Fragment(), HotelClickInterface {

    var arrayList: ArrayList<HotelEntity> = ArrayList()
    lateinit var binding: FragmentHotelsBinding
    lateinit var mainActivity: MainActivity
    lateinit var hotelListAdapter: HotelListAdapter
    lateinit var hotelEntity: HotelEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHotelsBinding.inflate(layoutInflater)
        hotelEntity = HotelEntity()
        val dialogBinding = HotelsenttryBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        hotelListAdapter = HotelListAdapter(arrayList, this@HotelsFragment)
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = hotelListAdapter

        binding.fabButton.setOnClickListener {
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )

            dialogBinding.tvStartDATE.setOnClickListener {
                val datePicker = DatePickerDialog(
                    requireContext(),
                    { view, year, month, date ->
                        val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DATE, date)
                        dialogBinding.tvStartDATE.setText(simpleDateFormat.format(selectedDate.time))
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePicker.show()

            }
            dialogBinding.tvEndDate.setOnClickListener {
                val datePicker = DatePickerDialog(
                    requireContext(),
                    { view, year, month, date ->
                        val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DATE, date)
                        dialogBinding.tvEndDate.setText(simpleDateFormat.format(selectedDate.time))
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
                )
                datePicker.show()

            }


            dialogBinding.btnAddHotel.setOnClickListener {
                hotelEntity.travel_id = mainActivity.travelDataEntity.Id
                hotelEntity.hotelName = dialogBinding.etHotelName.text.toString()
                hotelEntity.startDate = dialogBinding.tvStartDATE.text.toString()
                hotelEntity.charges = dialogBinding.etEnterCharges.text.toString()
                hotelEntity.endDate = dialogBinding.tvEndDate.text.toString()
                hotelEntity.address = dialogBinding.etEnterAddress.text.toString()
//                if (dialogBinding.checkbox.isChecked){
//                    hotelEntity.Status = "Traveled"
//                }
//                else{
//                    hotelEntity.Status = "Not Traveled"
//                }

                addHotelDetails(hotelEntity)
                dialog.dismiss()
            }
            dialog.show()
        }
        getHotelList()
        return binding.root

    }

    override fun HotelClicked(hotelEntity: HotelEntity) {
        updateHotelDetails(hotelEntity)
    }

    fun addHotelDetails(hotelEntity: HotelEntity) {
        class HotelInfo : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                getDatabase(requireContext()).travelDataDao().insertHotel(hotelEntity)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                arrayList.clear()
                getHotelList()
                Toast.makeText(requireContext(), "Hotel Details Added", Toast.LENGTH_SHORT).show()
            }
        }
        HotelInfo().execute()
    }


    fun getHotelList() {
        class GetHotels : AsyncTask<Void, Void, Void>() {
            val travel_id = mainActivity.travelDataEntity.Id
            override fun doInBackground(vararg params: Void?): Void? {

                id.let {
                    arrayList.addAll(
                        getDatabase(requireContext()).travelDataDao().getHotels(travel_id)
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
                    hotelListAdapter.notifyDataSetChanged()
                }


            }

        }
        GetHotels().execute()
    }


    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    fun updateHotelDetails(hotelEntity: HotelEntity) {
        val dialogHotelUpdateBinding = HotelsenttryupdateBinding.inflate(layoutInflater)
        val updateHotelDialog = Dialog(requireContext())
        updateHotelDialog.setContentView(dialogHotelUpdateBinding.root)
        val etHotelName = updateHotelDialog.findViewById<EditText>(R.id.etHotelName)
        val etEnterAddress = updateHotelDialog.findViewById<EditText>(R.id.etEnterAddress)
        val etEnterCharges = updateHotelDialog.findViewById<EditText>(R.id.etEnterCharges)
        val tvStartDATE = updateHotelDialog.findViewById<TextView>(R.id.tvStartDATE)
        val tvEndDate = updateHotelDialog.findViewById<TextView>(R.id.tvEndDate)


        val btnOk = updateHotelDialog.findViewById<Button>(R.id.btnAddHotel)
        val btnCancel = updateHotelDialog.findViewById<Button>(R.id.btnClose)

        updateHotelDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,)

        etHotelName.setText(hotelEntity.hotelName)
        etEnterAddress.setText(hotelEntity.address)
        etEnterCharges.setText(hotelEntity.charges)
        tvStartDATE.setText(hotelEntity.startDate)
        tvEndDate.setText(hotelEntity.endDate)

        updateHotelDialog.setCancelable(true)
        tvStartDATE.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    tvStartDATE.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()

        }
        tvEndDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    tvEndDate.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()
        }



        btnCancel.setOnClickListener {
            updateHotelDialog.dismiss()
        }

        btnOk.setOnClickListener {
            class UpdateTravelRecord : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    hotelEntity.hotelName = etHotelName.text.toString()
                    hotelEntity.address = etEnterAddress.text.toString()
                    hotelEntity.charges = etEnterCharges.text.toString()
                    hotelEntity.startDate = tvStartDATE.text.toString()
                    hotelEntity.endDate = tvEndDate.text.toString()
                    id.let {
                        getDatabase(requireContext()).travelDataDao().updateHotelData(hotelEntity)
                    }
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    Toast.makeText(requireContext(), "Hotel History Updated!", Toast.LENGTH_SHORT)
                        .show()
                    arrayList.clear()
                    getHotelList()
                }
            }
            UpdateTravelRecord().execute()

            updateHotelDialog.dismiss()
        }
        updateHotelDialog.show()
    }


    override fun deleteItem(hotelEntity: HotelEntity) {
        delPopUp(hotelEntity)
    }

    fun delPopUp(hotelEntity: HotelEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Hotel Details")
        alertDialog.setMessage("Do you want to clear this Hotel History?")

        alertDialog.setNegativeButton("No") { _, _ -> }

        alertDialog.setPositiveButton("Yes") { _, _ ->
            class deleteN : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg p0: Void?): Void? {
                    getDatabase(requireContext()).travelDataDao()
                        .deleteHotel(hotelEntity)
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    Toast.makeText(requireContext(), "Hotel History Delete", Toast.LENGTH_SHORT)
                        .show()
                    arrayList.clear()
                    getHotelList()
                }
            }

            deleteN().execute()

        }
        alertDialog.show()
    }
}
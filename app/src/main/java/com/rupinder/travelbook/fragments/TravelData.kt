package com.rupinder.travelbook.fragments

import android.app.DatePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.databinding.FragmentTravelDataBinding
import com.rupinder.travelbook.models.TravelDataEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase
import java.text.SimpleDateFormat
import java.util.*


class TravelData : Fragment() {

    private lateinit var btnAdd: Button
    lateinit var binding: FragmentTravelDataBinding
    lateinit var initView: View
    lateinit var mainActivity: MainActivity


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
        binding = FragmentTravelDataBinding.inflate(layoutInflater)

        binding.tvStartDATE.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    binding.tvStartDATE.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()

        }
        binding.tvEndDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, month, date ->
                    val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DATE, date)
                    binding.tvEndDate.setText(simpleDateFormat.format(selectedDate.time))
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
            )
            datePicker.show()

        }
        binding.btnAdd.setOnClickListener {
            if (binding.etEnterplace.text.toString().isNullOrEmpty()) {
                binding.etEnterplace.setError("Enter Place")
                binding.etEnterplace.requestFocus()
            } else {
                saveData()
            }
        }

        return binding.root
    }

    private fun saveData() {
        class save : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val travelDataEntity = TravelDataEntity()
                travelDataEntity.PLACE = binding.etEnterplace.text.toString()
                travelDataEntity.STARTDATE = binding.tvStartDATE.text.toString()
                travelDataEntity.ENDDATE = binding.tvEndDate.text.toString()

                if (binding.checkbox.isChecked){
                    travelDataEntity.Status = "Traveled"
                }
                else{
                    travelDataEntity.Status = "Not Traveled"
                }
                TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                    .insetdata(travelDataEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), "Save Successfully!", Toast.LENGTH_SHORT).show()
                mainActivity.navController.popBackStack()
            }
        }
        save().execute()
    }



}
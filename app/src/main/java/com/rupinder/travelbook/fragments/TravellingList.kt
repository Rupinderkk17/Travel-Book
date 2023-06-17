package com.rupinder.travelbook.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.R
import com.rupinder.travelbook.RecyclerClick
import com.rupinder.travelbook.adapters.TravelDataAdapter
import com.rupinder.travelbook.roomdb.TravelDataDatabase.Companion.getDatabase


import com.rupinder.travelbook.databinding.FragmentTravellingListBinding
import com.rupinder.travelbook.models.TravelDataEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase


class TravellingList : Fragment(), RecyclerClick {
    // TODO: Rename and change types of parameters

    private lateinit var fabButton: FloatingActionButton
    lateinit var binding: FragmentTravellingListBinding
    lateinit var initView: View
    lateinit var mainActivity: MainActivity
    var arrayList: ArrayList<TravelDataEntity> = ArrayList()
    lateinit var adapter: TravelDataAdapter



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
        // Inflate the layout for this fragment
        binding = FragmentTravellingListBinding.inflate(layoutInflater)
        adapter = TravelDataAdapter(arrayList, this)
        binding.listView.layoutManager =LinearLayoutManager(requireContext())
        binding.listView.adapter=adapter
        binding.fabButton.setOnClickListener{
            mainActivity.navController.navigate(R.id.travelData2)
        }
        getData()
        return binding.root
    }
    private fun getData() {
        arrayList.clear()
        class deleteN: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
             arrayList.addAll( TravelDataDatabase.getDatabase(requireContext()).travelDataDao()
                  .getTravelData())
                return null
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                if(arrayList.size==0){
                    binding.layoutNodata.visibility=View.VISIBLE
                    binding.listView.visibility=View.GONE
                }else{
                    binding.layoutNodata.visibility=View.GONE
                    binding.listView.visibility=View.VISIBLE
//                    Toast.makeText(requireContext(), "show Data", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                }
            }
        }

        deleteN().execute()
    }


    override fun TravelDataClicked(travelDataEntity: TravelDataEntity) {
            mainActivity.navController.navigate(R.id.searchoptions)
        mainActivity.travelDataEntity = travelDataEntity
        }

    override fun TravelDataDeleteClicked(travelDataEntity: TravelDataEntity) {
        delPopUp(travelDataEntity)
    }

    override fun UpdateTravelData(travelDataEntity: TravelDataEntity) {
       updateTravelData(travelDataEntity)
    }

    private fun deletedata(travelDataEntity: TravelDataEntity) {
        class deleteN: AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                getDatabase(requireContext()).travelDataDao()
                            .delete(travelDataEntity)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), "Trevel History Delete", Toast.LENGTH_SHORT).show()
                getData()
            }
        }

        deleteN().execute()

    }


    fun  delPopUp(travelDataEntity: TravelDataEntity){
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Traveling")
        alertDialog.setMessage("Do you want to delete this Traveling?")

        alertDialog.setPositiveButton("Yes"){_,_->
            deletedata(travelDataEntity)
        }
        alertDialog.setNegativeButton("No"){_,_-> }
        alertDialog.show()
    }

    fun updateTravelData(tdEntity: TravelDataEntity) {

        val dialogview = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_update_travel_data, null)
        val etPlace = dialogview.findViewById<EditText>(R.id.etEnterplace)


        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogview)
        val btnOk = dialogview.findViewById<Button>(R.id.btnUpdate)
        val btnCancel = dialogview.findViewById<Button>(R.id.btnClose)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )

        etPlace.setText(tdEntity.PLACE.toString())

        dialog.setCancelable(true)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        btnOk.setOnClickListener {
            // Update text
            tdEntity.PLACE = etPlace.text.toString()

            class UpdateTravelRecord : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    id.let { getDatabase(requireContext()).travelDataDao().updateTravelData(tdEntity) }
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    getData()
                }
            }
            UpdateTravelRecord().execute()

            dialog.dismiss()
        }
        dialog.show()
    }

}


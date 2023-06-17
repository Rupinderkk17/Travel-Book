package com.rupinder.travelbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.R
import com.rupinder.travelbook.databinding.FragmentSearchoptionsBinding


class Searchoptions : Fragment() {
    lateinit var binding: FragmentSearchoptionsBinding
    lateinit var initView: View
    lateinit var tvHotels: TextView
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
        binding = FragmentSearchoptionsBinding.inflate(layoutInflater)
        mainActivity.supportActionBar?.setTitle(mainActivity.travelDataEntity.PLACE.toString())

        binding.cvHotel.setOnClickListener {
            mainActivity.navController.navigate(R.id.hotels)
        }
        binding.cvFood.setOnClickListener {
            mainActivity.navController.navigate(R.id.action_searchoptions_to_foodFragment)
        }
        binding.cvTravelBy.setOnClickListener {
            mainActivity.navController.navigate(R.id.action_searchoptions_to_travelByFragment)
        }
        binding.cvTourPlaces.setOnClickListener {
            mainActivity.navController.navigate(R.id.tourplacesFragment)
        }
        binding.cvMemories.setOnClickListener {
            mainActivity.navController.navigate(R.id.memoriesFragment)
        }
            return binding.root
        }

    }

package com.rupinder.travelbook.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rupinder.travelbook.MainActivity
import com.rupinder.travelbook.adapters.MemoriesAdapter
import com.rupinder.travelbook.databinding.FragmentMemoriesBinding
import com.rupinder.travelbook.databinding.LayoutPreviewImageBinding
import com.rupinder.travelbook.memoriesClickInterface
import com.rupinder.travelbook.models.MemoriesEntity
import com.rupinder.travelbook.roomdb.TravelDataDatabase
import java.io.File
import java.util.concurrent.ExecutorService


class MemoriesFragment : Fragment(), memoriesClickInterface {

    private lateinit var safeContext: Context
    lateinit var binding: FragmentMemoriesBinding
    lateinit var dialogBinding: LayoutPreviewImageBinding
    lateinit var mainActivity: MainActivity
    var arrayList = ArrayList<MemoriesEntity>()
    lateinit var memoriesAdapter: MemoriesAdapter
    lateinit var gridLayoutManager: GridLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
        mainActivity = activity as MainActivity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMemoriesBinding.inflate(layoutInflater)
        dialogBinding = LayoutPreviewImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        memoriesAdapter = MemoriesAdapter(mainActivity, arrayList, this)
        gridLayoutManager = GridLayoutManager(mainActivity, 2)
        binding.listView.layoutManager = gridLayoutManager
        binding.listView.adapter = memoriesAdapter
        getMomeries()
        // Request camera permissions
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Setup the listener for take photo button
        binding.fabButton.setOnClickListener { takePhoto() }
    }


    var mCameraLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        // ---> Here the result code is always 0 <----
        val data = result.data
        System.out.println("data in launcher ${result.data}")
        if (result.resultCode == RESULT_OK && data != null) {
           /* val photo = data.extras!!["data"] as Bitmap?
            binding.rvMemories.setImageBitmap(photo)*/
            data?.extras?.let {
                previewImage(it["data"] as Bitmap)
            }
        }
    }

    fun previewImage(image: Bitmap){
        var dialog = Dialog(mainActivity)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogBinding.ivPreview.setImageBitmap(image)
        dialogBinding.btnSave.visibility = View.VISIBLE
        dialogBinding.btnClose.visibility = View.VISIBLE
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnSave.setOnClickListener {
            var memoriesEntity = MemoriesEntity()
            memoriesEntity.travel_Id = mainActivity.travelDataEntity.Id
            memoriesEntity.memories = mainActivity.encodeTobase64(image)
            class memories : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    TravelDataDatabase.getDatabase(requireContext()).travelDataDao().addMemories(memoriesEntity)
                    return null
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    arrayList.clear()
                    getMomeries()
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Memories Added", Toast.LENGTH_SHORT).show()
                }
            }
            memories().execute()
        }
        dialog.show()
    }

    fun getMomeries() {
        class getMemories : AsyncTask<Void, Void, Void>() {
            val travel_id = mainActivity.travelDataEntity.Id
            override fun doInBackground(vararg params: Void?): Void? {

                id.let {
                    arrayList.addAll(
                        TravelDataDatabase.getDatabase(requireContext()).travelDataDao().getMemoriesEntity(travel_id)
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
                    memoriesAdapter.notifyDataSetChanged()
                }
            }
        }
        getMemories().execute()
    }


    private fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mCameraLauncher.launch(cameraIntent)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                takePhoto()
            } else {
                Toast.makeText(safeContext, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
//                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    companion object {
        val TAG = "CameraXFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun deleteMemory(memoriesEntity: MemoriesEntity) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete Memories Details")
        alertDialog.setMessage("Do you want to delete this memory Details ?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            class deleteMemoriesEntity : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    TravelDataDatabase.getDatabase(requireContext()).travelDataDao().deleteMemories(memoriesEntity)
                    return null
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    arrayList.clear()
                    getMomeries()

                    Toast.makeText(requireContext(), "Memories Deleted!", Toast.LENGTH_SHORT).show()
                }
            }
            deleteMemoriesEntity().execute()
        }
        alertDialog.setNegativeButton("No") { _, _ -> }
        alertDialog.show()

    }

    override fun viewMemory(memoriesEntity: MemoriesEntity) {
        var dialog = Dialog(mainActivity)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogBinding.ivPreview.setImageBitmap(mainActivity.decodeBase64(memoriesEntity.memories))
        dialogBinding.btnSave.visibility = View.GONE
        dialogBinding.btnClose.visibility = View.VISIBLE
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}



// https://rick38yip.medium.com/camerax-on-android-fragment-in-kotlin-with-imageanalyzer-9cb58f9182a8
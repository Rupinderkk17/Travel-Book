package com.rupinder.travelbook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.rupinder.travelbook.models.TravelDataEntity
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    var travelDataEntity: TravelDataEntity = TravelDataEntity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController=findNavController(R.id.navController)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        supportActionBar?.setTitle(R.string.app_name)
    }

    //use this method to convert the selected image to bitmap to save in database
    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        return imageEncoded
    }

    //use this method to convert the saved string to bitmap
    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }
}
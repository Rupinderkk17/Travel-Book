package com.rupinder.travelbook.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rupinder.travelbook.*
import com.rupinder.travelbook.models.MemoriesEntity


class MemoriesAdapter(var mainActivity: MainActivity, var memoriesEntity:  ArrayList<MemoriesEntity>, var recyclerClick: memoriesClickInterface): RecyclerView.Adapter<MemoriesAdapter.ViewHolder>() {
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
       var image = view.findViewById<ImageView>(R.id.image)
       var fabDelete = view.findViewById<FloatingActionButton>(R.id.fabDelete)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
   ): ViewHolder {
              val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.layout_memories, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageBitmap(mainActivity.decodeBase64(memoriesEntity[position].memories))


        holder.fabDelete.setOnClickListener {
            recyclerClick.deleteMemory(memoriesEntity[position])
        }

        holder.itemView.setOnClickListener {
            recyclerClick.viewMemory(memoriesEntity[position])
        }
    }

    override fun getItemCount(): Int {
        return memoriesEntity.size
    }
}



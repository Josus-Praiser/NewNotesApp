package com.josus.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.josus.notesapp.R
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.UserWithNotes

class NotesAdapter(private val dataset:List<UserWithNotes>) : RecyclerView.Adapter<NotesAdapter.ItemViewHolder>() {

    class ItemViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val titleText:TextView = view.findViewById(R.id.notes_title)
        val descriptionText : TextView = view.findViewById(R.id.notes_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset.first().notes
        val notes = item[position]
        holder.titleText.text = notes.title
        holder.descriptionText.text = notes.description
    }

    override fun getItemCount(): Int {
        return dataset[0].notes.size
    }
}
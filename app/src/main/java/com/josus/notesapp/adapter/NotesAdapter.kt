package com.josus.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josus.notesapp.R
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.UserWithNotes
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ItemViewHolder>() {
//private val dataset:List<UserWithNotes>
    inner class ItemViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val titleText:TextView = view.findViewById(R.id.notes_title)
        val descriptionText : TextView = view.findViewById(R.id.notes_description)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Notes>(){
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.notesId == newItem.notesId
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_notes,parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val notes = differ.currentList[position]
       // val item = dataset.first().notes
       // val notes = item[position]
        holder.itemView.apply {
            this.notes_title.text = notes.title
            this.notes_description.text = notes.description
            setOnClickListener{
                onItemClickListener?.let {
                    it(notes)
                }
            }
        }
        //holder.titleText.text = notes.title
        //holder.descriptionText.text = notes.description

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
        //dataset[0].notes.size
    }

    private var onItemClickListener : ((Notes) -> Unit)? = null

    fun setOnItemClickListener(listener : (Notes) -> Unit){
        onItemClickListener = listener
    }
}
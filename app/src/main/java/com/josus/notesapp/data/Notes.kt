package com.josus.notesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val notesId : Int?=null,
    val title : String,
    val description : String,
    val userId:Int
)
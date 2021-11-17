package com.josus.notesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "userDetails"
)
data class User (
    @PrimaryKey(autoGenerate = true)
    var userId : Int?=null,
    var userName : String
        ) : Serializable

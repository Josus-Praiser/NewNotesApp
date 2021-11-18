package com.josus.notesapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(
    tableName = "userDetails"
)
data class User (
    @PrimaryKey
    val userId:Int,
    var userName : String,
    val isNewUser:Boolean = true,
    val userN: String
        ) : Serializable

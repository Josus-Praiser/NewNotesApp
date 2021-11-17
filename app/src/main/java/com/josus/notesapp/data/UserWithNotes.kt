package com.josus.notesapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithNotes(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val notes :List<Notes>
)
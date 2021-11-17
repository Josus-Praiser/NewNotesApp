package com.josus.notesapp.repository

import androidx.lifecycle.LiveData
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.User
import com.josus.notesapp.data.UserWithNotes
import com.josus.notesapp.database.LocalDatabase
import kotlinx.coroutines.flow.Flow

class UserRepository(
    val db:LocalDatabase
) {
    suspend fun upsert(user:User) = db.getUserDao().upsert(user)

     fun getUserDetails() = db.getUserDao().getUserDetails()

    suspend fun upsertNotes(notes:Notes) = db.getNotesDao().upsertNotes(notes)

    fun getAllNotes(userId:Int): LiveData<List<UserWithNotes>>{
        return db.getNotesDao().getAllNotes(userId)
    }
}
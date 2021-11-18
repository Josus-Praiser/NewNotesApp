package com.josus.notesapp.repository

import androidx.lifecycle.LiveData
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.User
import com.josus.notesapp.data.UserWithNotes
import com.josus.notesapp.database.LocalDatabase
import kotlinx.coroutines.flow.Flow
import java.util.*

class UserRepository(
    val db:LocalDatabase
) {
    suspend fun upsert(user:User) = db.getUserDao().upsert(user)

     fun getUserDetails(userName:String) = db.getUserDao().getUserDetails(userName)

    suspend fun upsertNotes(notes:Notes) = db.getNotesDao().upsertNotes(notes)

    fun getAllNotes(userId:Int): LiveData<List<UserWithNotes>>{
        return db.getNotesDao().getAllNotes(userId)
    }

    suspend fun deleteNotes(notes:Notes) = db.getNotesDao().deleteNotes(notes)
}
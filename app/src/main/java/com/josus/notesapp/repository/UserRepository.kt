package com.josus.notesapp.repository

import androidx.lifecycle.LiveData
import com.josus.notesapp.data.*
import com.josus.notesapp.database.LocalDatabase
import com.josus.notesapp.database.LocalDatabaseHelper

class UserRepository(
    val db: LocalDatabase,val newDb:LocalDatabaseHelper
) {
    suspend fun upsert(user: User) = db.getUserDao().upsert(user)

    fun getUserDetails(userName: String) = db.getUserDao().getUserDetails(userName)

    suspend fun upsertNotes(notes: Notes) = db.getNotesDao().upsertNotes(notes)

    fun getAllNotes(userId: Int): LiveData<List<UserWithNotes>> {
        return db.getNotesDao().getAllNotes(userId)
    }

    suspend fun deleteNotes(notes: Notes) = db.getNotesDao().deleteNotes(notes)

   suspend fun addUser(user:UserN) = newDb.addUser(user)

 suspend fun getUser(userId:String) = newDb.getUserById(userId)

    fun insertNotes(notes:NotesN) = newDb.insertNotes(notes)

    fun getAllNotesN(userId:String) = newDb.getAllNotes(userId)
}
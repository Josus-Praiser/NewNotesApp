package com.josus.notesapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import com.josus.notesapp.data.*

class LocalDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $TABLE_USER (" +
                "$COL_UID INTEGER PRIMARY KEY," +
                "$COL_USER_NAME TEXT);"

        val createNotesTable = "CREATE TABLE $TABLE_NOTES (" +
                "$COL_NID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_TITLE TEXT," +
                "$COL_NOTES TEXT," +
                "$COL_USER_N_ID INTEGER);"

        db?.execSQL(createUserTable)
        db?.execSQL(createNotesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

   suspend fun addUser(user: UserN) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.apply {
            put(COL_UID, user.userId)
            put(COL_USER_NAME, user.userName)
        }
        db.insert(TABLE_USER, null, cv)
    }


   suspend fun getUserById(userId: String):UserN {
        var user:UserN?=null
        val db = readableDatabase
        val results = db.rawQuery("SELECT * FROM $TABLE_USER WHERE userId = $userId", null)
        if (results.moveToNext()){
            val savedUserId = results.getString(results.getColumnIndexOrThrow(COL_UID))
            val userName = results.getString(results.getColumnIndexOrThrow(COL_USER_NAME))
            if (savedUserId=="-1"){
                user = UserN(savedUserId, userName)
            }
        }
        results.close()
        return user!!
    }

    fun insertNotes(notes: NotesN) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.apply {
            put(COL_TITLE, notes.title)
            put(COL_NOTES, notes.notes)
            put(COL_USER_N_ID, notes.userId)
        }
        db.insert(TABLE_NOTES, null, cv)
    }

   // fun deleteNotes(notes: NotesN) {}

    fun getAllNotes(userId: String):MutableList<NotesN>{
        val notesList: MutableList<NotesN> =ArrayList()
        val db = readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_NOTES WHERE userId = $userId", null)
        if (result.moveToFirst()){
            do {
                val noteId = result.getInt(result.getColumnIndexOrThrow(COL_NID))
                val title = result.getString(result.getColumnIndexOrThrow(COL_TITLE))
                val notes = result.getString(result.getColumnIndexOrThrow(COL_NOTES))
                val userNId = result.getString(result.getColumnIndexOrThrow(COL_USER_N_ID))
                val savedNotes = NotesN(noteId,title,notes,userNId)
                notesList.add(savedNotes)
            } while (result.moveToNext())
        }
        result.close()
        return notesList
    }
}
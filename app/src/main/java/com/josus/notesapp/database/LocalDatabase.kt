package com.josus.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.User

@Database(
    entities = [User::class,Notes::class],
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun getUserDao():UserDao

    abstract fun getNotesDao():NotesDao

    companion object{
        @Volatile
        private var instance : LocalDatabase?=null
        private var LOCK = Any()

        operator fun invoke(context:Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "userDetails_db.db"
            )
                .build()
    }
}
package com.josus.notesapp.database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.UserWithNotes
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNotes(notes: Notes)


    @Transaction
    @Query("SELECT * FROM userDetails WHERE userId = :userId")
    fun getAllNotes(userId:Int) : LiveData<List<UserWithNotes>>

    @Delete
    suspend fun deleteNotes(notes:Notes)
}
package com.josus.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josus.notesapp.data.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user:User)

    @Query("SELECT * FROM userDetails")
     fun getUserDetails():LiveData<User>


}
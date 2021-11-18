package com.josus.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.josus.notesapp.data.User


@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsert(user:User)

    @Query("SELECT * FROM userDetails WHERE userName = :userName")
     fun getUserDetails(userName:String): LiveData<User>


}
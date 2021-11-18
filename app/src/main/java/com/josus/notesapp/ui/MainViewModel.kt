package com.josus.notesapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.User
import com.josus.notesapp.data.UserWithNotes
import com.josus.notesapp.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext

class MainViewModel(private val userRepository: UserRepository) : ViewModel()
{



      fun saveUserDetails(user:User) = viewModelScope.launch {userRepository.upsert(user)  }


     fun getUserDetails(userName:String) = userRepository.getUserDetails(userName)


    fun upsertNotes(notes:Notes) = viewModelScope.launch {
        userRepository.upsertNotes(notes)
    }

   fun getAllNotes(userId:Int) :LiveData<List<UserWithNotes>> {
       return userRepository.getAllNotes(userId)
   }

    fun deleteNotes(notes:Notes) = viewModelScope.launch {
        userRepository.deleteNotes(notes)
    }
}
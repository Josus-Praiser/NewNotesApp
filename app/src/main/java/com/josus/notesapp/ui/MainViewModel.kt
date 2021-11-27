package com.josus.notesapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josus.notesapp.data.*
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

    fun addUser(user:UserN) = viewModelScope.launch { userRepository.addUser(user) }

 fun getUser(userId:String) = viewModelScope.launch { userRepository.getUser(userId) }

    fun insertNotes(notes:NotesN) = userRepository.insertNotes(notes)

    fun getAllNotesN(userId:String) = userRepository.getAllNotesN(userId)
}
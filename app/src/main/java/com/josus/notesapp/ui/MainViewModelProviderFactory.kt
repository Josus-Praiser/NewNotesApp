package com.josus.notesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josus.notesapp.repository.UserRepository

class MainViewModelProviderFactory(private val userRepository:UserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        return MainViewModel(userRepository) as T
    }
}
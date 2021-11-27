package com.josus.notesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.ViewModelProvider
import com.josus.notesapp.R
import com.josus.notesapp.database.LocalDatabase
import com.josus.notesapp.database.LocalDatabaseHelper
import com.josus.notesapp.repository.UserRepository

class MainActivity : AppCompatActivity() {

    var mainViewModel : MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userRepository = UserRepository(LocalDatabase(this), LocalDatabaseHelper(this))
        val viewModelProviderFactory = MainViewModelProviderFactory(userRepository)

        mainViewModel = ViewModelProvider(this,viewModelProviderFactory).get(MainViewModel::class.java)

    }


}
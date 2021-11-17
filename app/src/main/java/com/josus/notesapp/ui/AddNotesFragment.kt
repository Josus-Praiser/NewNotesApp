package com.josus.notesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.josus.notesapp.R
import com.josus.notesapp.data.Notes
import com.josus.notesapp.databinding.FragmentAddNotesBinding


class AddNotesFragment : Fragment() {

    private var _binding : FragmentAddNotesBinding? = null
    private val binding get() = _binding!!
    private var viewModel:MainViewModel?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_notes, container, false)
        _binding = FragmentAddNotesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).mainViewModel
        initObservers()

    }

    private fun initObservers(){

        viewModel?.getUserDetails()?.observe(viewLifecycleOwner, Observer { user->
            val userId = user.userId!!
            binding.saveNotesFab.setOnClickListener {
                val notesTitle = binding.etNotesTitle.text.toString()
                val notesDescription = binding.etNotesDescription.text.toString()
                val notes=Notes(null,notesTitle,notesDescription,userId)
                viewModel?.upsertNotes(notes)
                goToHomeScreen()
            }
        })
    }

    private fun goToHomeScreen(){
        findNavController().navigate(R.id.action_addNotesFragment_to_homeFragment)
    }

}
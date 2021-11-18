package com.josus.notesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.josus.notesapp.R
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.User
import com.josus.notesapp.databinding.FragmentAddNotesBinding


class AddNotesFragment : Fragment() {

    private var _binding : FragmentAddNotesBinding? = null
    private val binding get() = _binding!!
    private var viewModel:MainViewModel?=null
    val args : AddNotesFragmentArgs by navArgs()


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
        if (args.notes?.equals(null) == false){
            setUpEditTexts(args.notes)
        }
        else{
            initObservers(args.user)
        }
    }


    private fun initObservers(userR: User?){

        viewModel?.getUserDetails(userR?.userName!!)?.observe(viewLifecycleOwner, Observer { user->
            val userId = user.userId
            binding.saveNotesFab.setOnClickListener {
                val notesTitle = binding.etNotesTitle.text.toString()
                val notesDescription = binding.etNotesDescription.text.toString()
                val notes=Notes(null,notesTitle,notesDescription,userId)
                viewModel?.upsertNotes(notes)
                goToHomeScreen(user)
            }
        })
    }

    private fun goToHomeScreen(user:User?){
        val bundle = Bundle().apply {
            putSerializable("user",user)
        }
        findNavController().navigate(R.id.action_addNotesFragment_to_homeFragment,bundle)
    }


    private fun setUpEditTexts(notes: Notes?) {
         if (notes != null) {
            binding.etNotesTitle.setText(notes.title)
            binding.etNotesDescription.setText(notes.description)
        }
        viewModel?.getUserDetails(args.user?.userName!!)?.observe(viewLifecycleOwner, Observer { user->
            val userId = user.userId
            binding.saveNotesFab.setOnClickListener {
                val notesTitle = binding.etNotesTitle.text.toString()
                val notesDescription = binding.etNotesDescription.text.toString()
                val updatedNotes=Notes(notes?.notesId!!,notesTitle,notesDescription,userId)
                viewModel?.upsertNotes(updatedNotes)
                goToHomeScreen(user)
            }
        })
    }
}
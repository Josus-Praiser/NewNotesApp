package com.josus.notesapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.josus.notesapp.R
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.User
import com.josus.notesapp.databinding.FragmentAddNotesBinding


class AddNotesFragment : Fragment() {

    private var _binding: FragmentAddNotesBinding? = null
    private val binding get() = _binding!!
    private var viewModel: MainViewModel? = null
    val args: AddNotesFragmentArgs by navArgs()
    lateinit var sPref: SharedPreferences
    var userName: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_notes, container, false)
        _binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).mainViewModel
        sPref = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
        userName = sPref.getString("userName", "")!!
        if (args.notes?.equals(null) == false) {
            setUpEditTexts(args.notes)
        } else {
            initObservers(userName)
        }
    }


    private fun initObservers(userName: String) {

        viewModel?.getUserDetails(userName)?.observe(viewLifecycleOwner, Observer { user ->
            val userId = user.userId
            binding.saveNotesFab.setOnClickListener {
                if (validateEditTexts()) {
                    val notesTitle = binding.etNotesTitle.text.toString()
                    val notesDescription = binding.etNotesDescription.text.toString()
                    val notes = Notes(null, notesTitle, notesDescription, userId)
                    viewModel?.upsertNotes(notes)
                    goToHomeScreen(user)
                }

            }
        })
    }

    private fun goToHomeScreen(user: User?) {
        val bundle = Bundle().apply {
            putSerializable("user", user)
        }
        findNavController().navigate(R.id.action_addNotesFragment_to_homeFragment, bundle)
    }


    private fun setUpEditTexts(notes: Notes?) {
        if (notes != null) {
            binding.etNotesTitle.setText(notes.title)
            binding.etNotesDescription.setText(notes.description)
        }
        viewModel?.getUserDetails(userName)?.observe(viewLifecycleOwner, Observer { user ->
            val userId = user.userId
            binding.saveNotesFab.setOnClickListener {
                if (validateEditTexts()) {
                    val notesTitle = binding.etNotesTitle.text.toString()
                    val notesDescription = binding.etNotesDescription.text.toString()
                    val updatedNotes = Notes(notes?.notesId!!, notesTitle, notesDescription, userId)
                    viewModel?.upsertNotes(updatedNotes)
                    goToHomeScreen(user)
                }

            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun validateEditTexts(): Boolean {
        val check: Boolean
        val notesTitle = binding.etNotesTitle.text.toString()
        val notesDescription = binding.etNotesDescription.text.toString()
        if (notesTitle.isEmpty()) {
            showToast("Enter a Title")
            check = false
        } else if (notesDescription.isEmpty()) {
            showToast("Enter notes")
            check = false
        } else {
            check = true
        }
        return check
    }
}
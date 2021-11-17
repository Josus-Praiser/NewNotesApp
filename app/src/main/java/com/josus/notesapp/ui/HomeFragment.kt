package com.josus.notesapp.ui

import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.josus.notesapp.R
import com.josus.notesapp.adapter.NotesAdapter
import com.josus.notesapp.data.Notes
import com.josus.notesapp.data.UserWithNotes
import com.josus.notesapp.databinding.FragmentHomeBinding
import java.lang.Exception

class HomeFragment : Fragment() {

    private var viewModel : MainViewModel? = null
    private var _binding : FragmentHomeBinding ? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter : NotesAdapter
    private var userId:Int =0

    val args : HomeFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).mainViewModel

         userId= args.user?.userId!!
        initObservers(userId)
        setUpClickListeners()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.logout_option ->{
             true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }



    private fun goToNextScreen(){
        findNavController().navigate(R.id.action_homeFragment_to_addNotesFragment)
    }

    private fun initObservers(userId:Int){

        /*
             try {
            viewModel?.getUserDetails()?.observe(viewLifecycleOwner, Observer { user ->
                 userId=user.userId!!
            } )
        }
        catch (e:Exception){
            binding.noNotesTxt.text = e.toString()
        }
         */



        try {
            viewModel?.getAllNotes(userId)?.observe(viewLifecycleOwner, Observer { notes->
                if (notes == null || notes.isEmpty() || notes[0].notes.isEmpty()){
                    binding.noNotesTxt.visibility = View.VISIBLE
                    binding.notesRecyclerView.visibility = View.GONE
                }
                else{
                    binding.noNotesTxt.visibility = View.GONE
                    binding.notesRecyclerView.visibility = View.VISIBLE
                    setUpRecyclerView(notes)
                }
            })
        }
        catch (e:Exception){
            binding.noNotesTxt.visibility = View.VISIBLE
            binding.notesRecyclerView.visibility = View.GONE
            binding.noNotesTxt.text = e.toString()
        }


    }

    private fun setUpClickListeners() {
        binding.addNotesFab.setOnClickListener {
            goToNextScreen()
        }
    }

    private fun setUpRecyclerView(data: List<UserWithNotes>){
        notesAdapter = NotesAdapter(data)
        binding.notesRecyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}
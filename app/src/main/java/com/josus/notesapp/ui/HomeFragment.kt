package com.josus.notesapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.josus.notesapp.R
import com.josus.notesapp.adapter.NotesAdapter
import com.josus.notesapp.data.UserWithNotes
import com.josus.notesapp.databinding.FragmentHomeBinding
import com.josus.notesapp.util.ConnectionManager

class HomeFragment : Fragment() {

    private var viewModel: MainViewModel? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter
    private var userId: Int? = null
    lateinit var sPref: SharedPreferences
    private var mGoogleSignInClient: GoogleSignInClient? = null
    val args: HomeFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).mainViewModel
        sPref = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
        val savedUser = args.user?.userName!!
        viewModel?.getUserDetails(savedUser)?.observe(viewLifecycleOwner, Observer {
            binding.userId.text = getString(R.string.userName_txt, it.userN)
            initObservers(it.userId)
        })

        /*
        val user =viewModel?.getUser(savedUser)
        binding.userId.text = getString(R.string.userName_txt,user?.userName)
        initObservers(user?.userId!!)
         */



        binding.addNotesFab.setOnClickListener {
            goToNextScreen(savedUser)
        }
        onItemSwiped(view)
        initGoogleSignInClient()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.logout_option -> {
            if (ConnectionManager().checkConnectivity(requireContext())) {
                sPref.edit().putBoolean("isLoggedIn", false).apply()
                try {
                    signOut()
                } catch (e: Exception) {
                    showToast(e.toString(), 1)
                }
            } else {
                showToast(noInternetMsg, 0)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    private fun goToNextScreen(userName: String) {
        viewModel?.getUserDetails(userName)?.observe(viewLifecycleOwner, Observer {
            val bundle = Bundle().apply {
                putSerializable("user", it)
                putSerializable("notes", null)
            }
            findNavController().navigate(R.id.action_homeFragment_to_addNotesFragment, bundle)
        })

    }

    private fun initObservers(userId: Int) {

        try {
            viewModel?.getAllNotes(userId)?.observe(viewLifecycleOwner, Observer { notes ->
                if (notes == null || notes.isEmpty() || notes[0].notes.isEmpty()) {
                    binding.noNotesTxt.visibility = View.VISIBLE
                    binding.notesRecyclerView.visibility = View.GONE
                } else {
                    binding.noNotesTxt.visibility = View.GONE
                    binding.notesRecyclerView.visibility = View.VISIBLE
                    setUpRecyclerView(notes)
                    notesAdapter.setOnItemClickListener {
                        val bundle = Bundle().apply {
                            putSerializable("user", null)
                            putSerializable("notes", it)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_addNotesFragment,
                            bundle
                        )
                    }
                }
            })
        } catch (e: Exception) {
            binding.noNotesTxt.visibility = View.VISIBLE
            binding.notesRecyclerView.visibility = View.GONE
            binding.userId.visibility = View.GONE
            binding.noNotesTxt.text = e.toString()
        }

        /*
         val notesList = viewModel?.getAllNotesN(userId)
        if (notesList == null){
            binding.noNotesTxt.visibility = View.VISIBLE
            binding.notesRecyclerView.visibility = View.GONE
        }
        else{
            binding.noNotesTxt.visibility = View.GONE
            binding.notesRecyclerView.visibility = View.VISIBLE
            setUpRecyclerView(notesList)
        }
         */


    }

    private fun setUpRecyclerView(data: List<UserWithNotes>) {
        notesAdapter = NotesAdapter()
        binding.notesRecyclerView.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            notesAdapter.differ.submitList(data.first().notes)
        }
    }


    private fun onItemSwiped(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val notes = notesAdapter.differ.currentList[position]
                viewModel?.deleteNotes(notes)
                Snackbar.make(view, "Notes Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel?.upsertNotes(notes)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.notesRecyclerView)
        }
    }

    private fun goToLoginScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun signOut() {
        if (ConnectionManager().checkConnectivity(requireContext())) {
            mGoogleSignInClient?.signOut()
                ?.addOnCompleteListener(requireActivity(), OnCompleteListener {
                    goToLoginScreen()
                })
        } else {
            showToast(noInternetMsg, 0)
        }

    }

    private fun initGoogleSignInClient() {
        if (ConnectionManager().checkConnectivity(requireContext())) {
            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        } else {
            showToast(noInternetMsg, 0)
        }

    }

    private fun showToast(msg: String, dur: Int) {
        Toast.makeText(requireContext(), msg, dur).show()
    }

    companion object {
        const val noInternetMsg = "No Internet Found"
    }
}
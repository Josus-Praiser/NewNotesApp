package com.josus.notesapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.josus.notesapp.R
import com.josus.notesapp.data.User
import com.josus.notesapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    var viewModel: MainViewModel? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var sPref: SharedPreferences
    private var savedUser :String? =""
    var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_login, container, false)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).mainViewModel
        val sPref = requireActivity().getSharedPreferences("Login", Context.MODE_PRIVATE)
        val isLoggedIn = sPref.getBoolean("isLoggedIn", false)
         savedUser = sPref.getString("userName","")


        Log.d("loggedIn", "Logged In is $isLoggedIn")

            if (isLoggedIn) {
                // val savedUser = viewModel?.getUserDetails()?.value
                goToNextScreen(savedUser!!)
            } else {
                initGoogleSignInClient()
                binding.btnLogin.setOnClickListener { signIn() }
            }


    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount: GoogleSignInAccount? =
                    task.getResult(ApiException::class.java)
                googleSignInAccount?.let {
                    sPref = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE)
                    sPref.edit().putBoolean("isLoggedIn", true)
                        .apply()

                    val userName = it.id!!
                    viewModel?.getUserDetails(userName)?.observe(viewLifecycleOwner, Observer { user->
                        if (user == null){
                            sPref.edit().putString("userName",it.id).apply()
                            val id = Random().nextInt()
                            val newUser = User(id,userName,false,it.givenName!!)
                            viewModel?.saveUserDetails(newUser)
                            goToNextScreen(userName)
                        }
                        else{
                            sPref.edit().putString("userName",user.userName).apply()
                            goToNextScreen(user.userName)
                        }
                    })

                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToNextScreen(userName:String) {
        job?.cancel()
        job = MainScope().launch {
            delay(500L)
            viewModel?.getUserDetails(userName)?.observe(viewLifecycleOwner, Observer { user ->
                val bundle = Bundle().apply {
                    putSerializable("user", user)
                }
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment, bundle)
            })
        }


    }

    companion object {
        const val RC_SIGN_IN = 123
    }

}
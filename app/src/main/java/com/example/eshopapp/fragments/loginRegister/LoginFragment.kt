package com.example.eshopapp.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.eshopapp.R
import com.example.eshopapp.activities.ShoppingActivity
import com.example.eshopapp.databinding.FragmentLoginBinding
import com.example.eshopapp.util.Resource
import com.example.eshopapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class LoginFragment :  Fragment(R.layout.fragment_login){
    val TAG: String = "LoginFragment"
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply{
            buttonLogin.setOnClickListener{
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email,password)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state and cancels it when it's STOPPED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Start listening for values.
                // This happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED

                viewModel.login.collect {
                    when(it){
                        is Resource.Loading -> {
                            binding.buttonLogin.startAnimation()

                        }
                        is Resource.Success -> {
                            Log.d(TAG,"GoogleSignIn:Successful")
                            val intent = Intent(activity,ShoppingActivity::class.java)
                            binding.buttonLogin.revertAnimation()
                            Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            binding.buttonLogin.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

package com.example.eshopapp.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.eshopapp.R
import com.example.eshopapp.data.User
import com.example.eshopapp.databinding.FragmentRegisterBinding
import com.example.eshopapp.util.Resource
import com.example.eshopapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment :  Fragment(){

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegister.setOnClickListener{
                val user = User(
                    edFirstName.text.toString().trim(),
                    edLastName.text.toString().trim(),
                    edEmail.text.toString().trim()
                )
                val password = edPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state and cancels it when it's STOPPED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Start listening for values.
                // This happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED

                viewModel.register.collect {
                    when(it){
                        is Resource.Loading -> {
                            binding.buttonRegister.startAnimation()
                        }
                        is Resource.Success -> {
                            Log.d("test", it.data.toString())
                            binding.buttonRegister.revertAnimation()
                        }
                        is Resource.Error -> {
                            Log.e(TAG, it.message.toString())
                            binding.buttonRegister.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}
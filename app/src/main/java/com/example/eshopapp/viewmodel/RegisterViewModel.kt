package com.example.eshopapp.viewmodel

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import com.example.eshopapp.data.User
import com.example.eshopapp.util.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel(){

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
     val register: Flow<Resource<FirebaseUser>> = _register

    fun createAccountWithEmailAndPassword(user: User, password: String){
        runBlocking {
            _register.emit(Resource.Loading())
        }
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                it.user?.let {
                    _register.value = Resource.Success(it)
                }
            } .addOnFailureListener{
                _register.value = Resource.Error(it.message.toString())
            }
    }

}
package com.tk.tktask.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tk.tktask.signup.repository.ISignUpRepository

class ViewModelFactory(
    private val repositoryX: ISignUpRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(repositoryX) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
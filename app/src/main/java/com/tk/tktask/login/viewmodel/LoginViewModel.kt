package com.tk.tktask.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tk.tktask.login.repository.LoginRepository
import com.tk.tktask.realmdb.enitity.UserEntity

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {
    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _emailError = MutableLiveData(false)
    val emailError: LiveData<Boolean> = _emailError

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _passwordError = MutableLiveData(false)
    val passwordError: LiveData<Boolean> = _passwordError


    fun setEmail(email: String) {
        _email.value = email
    }

    fun getEmail(): String {
        return email.value.orEmpty()
    }

    fun setEmailError(isError: Boolean) {
        _emailError.value = isError
    }

    fun getEmailError(): Boolean {
        return emailError.value == true
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun getPassword(): String {
        return password.value.orEmpty()
    }

    fun setPasswordError(isError: Boolean) {
        _passwordError.value = isError
    }

    fun getPasswordError(): Boolean {
        return passwordError.value == true
    }

    fun login(): Boolean {
        val userEntity = UserEntity().apply {
            this.email = _email.value.orEmpty()
            this.password = _password.value.orEmpty()
        }
        val loginSuccess = repository.loginUser(userEntity)
        if (loginSuccess) {
            Log.d("LoginViewModel", "Login successful for user: ${userEntity.email}")
            repository.logoutAllUsersExcept(userEntity)
        } else {
            Log.d("LoginViewModel", "Login failed for user: ${userEntity.email}")
        }
        return loginSuccess
    }
}
package com.tk.tktask.signup.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tk.tktask.realmdb.enitity.UserEntity
import com.tk.tktask.signup.repository.ISignUpRepository
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: ISignUpRepository
) : ViewModel() {
    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _emailError = MutableLiveData(false)
    val emailError: LiveData<Boolean> = _emailError

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _usernameError = MutableLiveData(false)
    val usernameError: LiveData<Boolean> = _usernameError

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _passwordError = MutableLiveData(false)
    val passwordError: LiveData<Boolean> = _passwordError

    private val _loggedInUser = MutableLiveData(false)
    val loggedInUser: LiveData<Boolean> = _loggedInUser

    private val _loggedInUserEntity: MutableLiveData<UserEntity?> = MutableLiveData(null)
    val loggedInUserEntity: LiveData<UserEntity?> = _loggedInUserEntity

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
        return (emailError.value == true)
    }

    fun setUsername(username: String) {
        _username.value = username
    }

    fun getUsername(): String {
        return username.value.orEmpty()
    }

    fun setUsernameError(isError: Boolean) {
        _usernameError.value = isError
    }

    fun getUsernameError(): Boolean {
        return (usernameError.value == true)
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
        return (passwordError.value == true)
    }

    fun addUser() {
        val mData = UserEntity().apply {
            this.email = this@SignUpViewModel.email.value.orEmpty()
            this.username = this@SignUpViewModel.username.value.orEmpty()
            this.password = this@SignUpViewModel.password.value.orEmpty()
        }
        try {
            repository.addUser(mData)
        } catch (e: RealmPrimaryKeyConstraintException) {
            e.printStackTrace()
            Log.e("TAG", "addUser: Already exit")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun findLoggedInUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.findLoginUser().apply {
                    if (this != null) {
                        _loggedInUserEntity.postValue(this)
                    }
                    _loggedInUser.postValue(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loggedInUser.postValue(true)
            }

        }
    }

    /*   fun getUsers() {
           val users = repository.getUsers()
           for (user in users) {
               Log.e(
                   "User",
                   "Email: ${user.email}, Username: ${user.username}, Password: ${user.password}"
               )
           }
       }*/

}
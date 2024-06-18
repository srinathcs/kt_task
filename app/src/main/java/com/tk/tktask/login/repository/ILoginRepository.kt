package com.tk.tktask.login.repository

import com.tk.tktask.realmdb.enitity.UserEntity

interface ILoginRepository {
    fun loginUser(model: UserEntity): Boolean
    fun logoutAllUsersExcept(model: UserEntity)

    suspend fun getAllUsers(): List<UserEntity>

}
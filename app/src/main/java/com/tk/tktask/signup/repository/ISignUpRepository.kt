package com.tk.tktask.signup.repository

import com.tk.tktask.realmdb.enitity.UserEntity

interface ISignUpRepository {
    fun addUser(model: UserEntity)

    fun loginUser(model: UserEntity)

    suspend fun findLoginUser(): UserEntity?
}
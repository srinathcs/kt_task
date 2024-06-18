package com.tk.tktask.signup.repository

import com.tk.tktask.realmdb.enitity.UserEntity
import com.tk.tktask.realmdb.tables.UserTableFields
import io.realm.Realm
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignUpRepository : ISignUpRepository {
    private val realm: Realm = Realm.getDefaultInstance()

    override fun addUser(model: UserEntity) {
        realm.executeTransaction {
            it.createObject(UserEntity::class.java, model.email).apply {
                username = model.username
                password = model.password
            }
        }
    }

    override fun loginUser(model: UserEntity) {
        val currentUser = realm.where(UserEntity::class.java)
            .equalTo(UserTableFields.IS_CURRENT_USER, true)
            .findFirst()

        currentUser?.isCurrentUser = false

        val user = realm.where(UserEntity::class.java)
            .equalTo(UserTableFields.USER_EMAIL, model.email)
            .equalTo(UserTableFields.USER_PASSWORD, model.password)
            .findFirst()

        user?.isCurrentUser = true

    }

    override suspend fun findLoginUser(): UserEntity? = suspendCoroutine {
        try {
            val currentUser = realm.where(UserEntity::class.java)
                .equalTo(UserTableFields.IS_CURRENT_USER, true)
                .findFirst()
            it.resume(currentUser)
        } catch (e: Exception) {
            e.printStackTrace()
            it.resume(null)
        }
    }
}
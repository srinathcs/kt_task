package com.tk.tktask.login.repository

import com.tk.tktask.realmdb.enitity.UserEntity
import com.tk.tktask.realmdb.tables.UserTableFields
import io.realm.Realm
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepository : ILoginRepository {
    val realm: Realm = Realm.getDefaultInstance()

    override fun loginUser(model: UserEntity): Boolean {
        val user = realm.where(UserEntity::class.java)
            .equalTo(UserTableFields.USER_EMAIL, model.email)
            .equalTo(UserTableFields.USER_PASSWORD, model.password)
            .findFirst()

        if (user != null) {
            realm.executeTransaction {
                user.isCurrentUser = true
                it.insertOrUpdate(user)
            }
            return true
        }
        return false
    }

    override fun logoutAllUsersExcept(model: UserEntity) {
        realm.executeTransaction {
            val users = it.where(UserEntity::class.java).findAll()
            users.forEach { user ->
                if (user.email != model.email) {
                    user.isCurrentUser = false
                    it.insertOrUpdate(user)
                }
            }
        }
    }

    override suspend fun getAllUsers(): List<UserEntity> = suspendCoroutine {
        realm.executeTransactionAsync { realmDb ->
            try {
                val data = realmDb.where(UserEntity::class.java).findAll()
                it.resume(data)
            } catch (e: Exception) {
                e.printStackTrace()
                it.resume(listOf())
            }
        }
    }
}

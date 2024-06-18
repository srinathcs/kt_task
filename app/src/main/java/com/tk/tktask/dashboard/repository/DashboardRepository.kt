package com.tk.tktask.dashboard.repository

import com.tk.tktask.realmdb.enitity.LocationEntity
import com.tk.tktask.realmdb.enitity.UserEntity
import com.tk.tktask.realmdb.tables.UserTableFields
import io.realm.Realm
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DashboardRepository : IDashboardRepository {
    private val realm: Realm = Realm.getDefaultInstance()
    override suspend fun getAllUsers(): List<UserEntity> = suspendCoroutine {
        realm.executeTransaction { realmDb ->
            try {
                val data = realmDb.where(UserEntity::class.java).findAll()
                it.resume(data)
            } catch (e: Exception) {
                e.printStackTrace()
                it.resume(listOf())
            }
        }
    }

    override suspend fun getCurrentUser(): UserEntity? = suspendCoroutine {
        realm.executeTransaction { realmDb ->
            try {
                val findFirst = realmDb.where(UserEntity::class.java)
                    .equalTo(UserTableFields.IS_CURRENT_USER, true)
                    .findFirst()
                it.resume(findFirst)
            } catch (e: Exception) {
                e.printStackTrace()
                it.resume(null)
            }
        }
    }

    override suspend fun getCurrentUserLocations(): List<LocationEntity> = suspendCoroutine {
        val currentUser = runBlocking { getCurrentUser() }
        realm.executeTransaction { realmDb ->
            if (currentUser != null) {
                val results = realmDb.where(LocationEntity::class.java)
                    .equalTo(UserTableFields.USER_EMAIL, currentUser.email)
                    .findAll()
                it.resume(results)
            } else {
                it.resume(listOf())
            }
        }
    }

    override suspend fun updateLocation(location: LocationEntity): Unit = suspendCoroutine {
        realm.executeTransaction { realmDb ->
            try {
                realmDb.insertOrUpdate(location)
                it.resume(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                it.resume(Unit)
            }
        }
    }

    override suspend fun getAllLocations(): List<LocationEntity> = suspendCoroutine {
        realm.executeTransaction { realmDb ->
            try {
                val results = realmDb.where(LocationEntity::class.java).findAll()
                it.resume(realm.copyFromRealm(results))
            } catch (e: Exception) {
                e.printStackTrace()
                it.resume(listOf())
            }
        }
    }
}
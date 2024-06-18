package com.tk.tktask.dashboard.repository

import com.tk.tktask.realmdb.enitity.LocationEntity
import com.tk.tktask.realmdb.enitity.UserEntity

interface IDashboardRepository {
    suspend fun getAllUsers(): List<UserEntity>

    suspend fun getCurrentUser(): UserEntity?

    suspend fun getCurrentUserLocations(): List<LocationEntity>

    suspend fun updateLocation(location: LocationEntity)

    suspend fun getAllLocations(): List<LocationEntity>
}
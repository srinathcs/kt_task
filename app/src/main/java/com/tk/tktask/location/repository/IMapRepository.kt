package com.tk.tktask.location.repository

import com.tk.tktask.realmdb.enitity.LocationEntity

interface IMapRepository {
    fun getCurrentUserLocationHistory(): List<LocationEntity>
}
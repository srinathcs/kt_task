package com.tk.tktask.location.repository

import com.tk.tktask.realmdb.enitity.LocationEntity
import io.realm.Realm
import io.realm.kotlin.where

class MapRepository : IMapRepository {
    override fun getCurrentUserLocationHistory(): List<LocationEntity> {
        val realm = Realm.getDefaultInstance()
        val locationEntities = realm.where<LocationEntity>().findAll()
        val locationList = realm.copyFromRealm(locationEntities)
        realm.close()

        return locationList
    }
}
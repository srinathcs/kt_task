package com.tk.tktask.realmdb.enitity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LocationEntity : RealmObject() {
    @PrimaryKey var timestamp: Long? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var email: String = ""
}
package com.tk.tktask.realmdb.enitity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserEntity : RealmObject() {
    @PrimaryKey var email: String = ""
    var username: String = ""
    var password: String = ""
    var isCurrentUser: Boolean = false
}
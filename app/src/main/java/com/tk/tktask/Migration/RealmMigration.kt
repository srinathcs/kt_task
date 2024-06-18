package com.tk.tktask.Migration

import io.realm.DynamicRealm
import io.realm.RealmMigration

class RealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        // Migration logic, for example:
        // if (oldVersion == 0L) {
        //     schema.get("MyDataModel")
        //         ?.addField("newField", String::class.java)
        //     oldVersion++
        // }
    }
}
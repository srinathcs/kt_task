package com.tk.tktask.realmdb

import android.app.Application
import com.tk.tktask.Migration.RealmMigration
import io.realm.Realm
import io.realm.RealmConfiguration

object RealmDB {

    fun init(application: Application) {
        Realm.init(application)
        val config = RealmConfiguration.Builder()
            .name("tk_task.realm")
            .schemaVersion(1)
            .allowWritesOnUiThread(true)
            .migration(RealmMigration())
            .maxNumberOfActiveVersions(100)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
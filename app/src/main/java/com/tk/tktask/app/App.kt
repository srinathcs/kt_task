package com.tk.tktask.app

import android.app.Application
import com.tk.tktask.manager.PrefManager
import com.tk.tktask.realmdb.RealmDB

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RealmDB.init(this)
        PrefManager.init(applicationContext)
    }
}
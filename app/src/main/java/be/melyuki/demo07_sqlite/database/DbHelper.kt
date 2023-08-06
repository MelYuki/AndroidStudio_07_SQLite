package be.melyuki.demo07_sqlite.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DbContract.NAME, null, DbContract.VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // onCreate du helper, on lui passe le script qui créé la table person
        db?.execSQL(DbContract.PersonTable.SCRIPT_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Migration simple (Ne pas faire en prod :D)
        // - On éfface tout
        db?.execSQL(DbContract.PersonTable.SCRIPT_DROP)
        // - On regénère tout
        onCreate(db)
    }
}
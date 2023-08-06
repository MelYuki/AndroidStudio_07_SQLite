package be.melyuki.demo07_sqlite.database.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import be.melyuki.demo07_sqlite.database.DbContract
import be.melyuki.demo07_sqlite.database.DbHelper
import be.melyuki.demo07_sqlite.models.Person
import java.time.LocalDate

// DAO -> Data Access Object
// *************************
// On créé ici le DAO de la table person
// Il faut donc autant de DAO que de table

class PersonDao(val context: Context) {

    // Import de notre DbHelper et du module SQLiteDatabase d'android
    var dbHelper : DbHelper? = null
    var db : SQLiteDatabase? = null

    // region Méthodes de connexion à la DB
    // Méthode d'écriture de la DB
    fun openWritable() : Unit {
        dbHelper = DbHelper(context)
        db = dbHelper?.writableDatabase
    }

    // Méthode de lecture de la DB
    fun openReadable() : Unit {
        dbHelper = DbHelper(context)
        db = dbHelper?.readableDatabase
    }

    // Méthode de fermeture de connexion
    fun close() : Unit {
        db?.close()
        dbHelper?.close()
    }
    // endregion

    // region Méthode "contentValues" & "cursor" d'android
    // ***************************************************
    // Création d'une méthode "contentValues"
    // -> nécessaire pour les méthodes d'insert et d'update
    private fun getContentValues(person: Person) : ContentValues {
        val contentValues = ContentValues().apply {
            put(DbContract.PersonTable.FIRST_NAME, person.firstname)
            put(DbContract.PersonTable.LAST_NAME, person.lastname)
            put(DbContract.PersonTable.BIRTH_DATE, person.birthDate.toString())
            put(DbContract.PersonTable.EMAIL, person.email)
            put(DbContract.PersonTable.PHONE_NBR, person.phone)
        }
        return contentValues
    }

    // Création d'une méthode "cursor"
    // -> nécessaire pour la méthode query, donc lire une ou plusieurs entrées de la table
    private fun cursorToPerson(cursor: Cursor) : Person {
        val person = Person(
            cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.PersonTable.ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PersonTable.FIRST_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PersonTable.LAST_NAME)),
            LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PersonTable.BIRTH_DATE))),
            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PersonTable.EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PersonTable.PHONE_NBR))
        )
        return person
    }
    // endregion

    // region Méthodes du "CRUD"
    // *************************
    // Méthode getAll
    // -> qui retournera la liste des Person
    fun getAll() : List<Person> {

        // Exécution de la requête
        val cursor = db!!.query(
            DbContract.PersonTable.TABLE_NAME,
            null, // Pour la liste des colonnes, null = SELECT *
            null, null, // selection & selectionArgs = Clauses where & whereArgs
            null, // Clause GroupBy
            null, // Clause Having
            "${DbContract.PersonTable.LAST_NAME} ASC, ${DbContract.PersonTable.FIRST_NAME} ASC"
        ) // -> Return : A Cursor object, which is positioned before the first entry.

        // Gestion du résultat "vide"
        if (cursor.count == 0) {
            // retour d'une liste vide
            return listOf()
        }
        // Gestion du résultat "non-vide"
        val result = mutableListOf<Person>()

        // - Positionner le cursor sur le premier élément
        cursor.moveToFirst()
        // Boucle de récupération des données, tant que le cursor n'est pas après le dernier
        while (!cursor.isAfterLast) {
            // Récup des data
            val person : Person = cursorToPerson(cursor)
            // Ajout dans la liste
            result.add(person)
            // - Positionner le cursor sur l'élément suivant
            cursor.moveToNext()
        }

        // - Fermeture du cursor
        cursor.close()

        // Retourner le résultat
        return result.toList()
    }

    // Méthode getById
    // -> qui aura en paramètre l'ID de la Person
    // -> qui retournera une seule Person
    fun getById(personId : Long) : Person? {

        // Exécution de la requête
        val cursor = db!!.query(
            DbContract.PersonTable.TABLE_NAME,
            null,
            DbContract.PersonTable.ID + " = ? ",
            arrayOf(personId.toString()),
            null,
            null,
            null
        )

        // Gestion du cas de l'élément "inexistant"
        if (cursor.count == 0) {
            return null
        }

        // - Positionner le cursor sur l'élément
        cursor.moveToFirst()
        // Gestion de cas de l'élément "existant"
        val result : Person = cursorToPerson(cursor)

        // - Fermeture du cursor
        cursor.close()

        // Retourner le résultat
        return result
    }

    // Méthode create
    // -> qui aura en paramètre une Person
    // -> qui retournera un ID de type Long
    fun create(person: Person) : Long {
        val id = db!!.insert(
            DbContract.PersonTable.TABLE_NAME,
            null,
            getContentValues(person)
        )
        return id
    }

    // Méthode update
    // -> qui aura en paramètre une Person
    // -> qui retournera un Boolean
    fun update(person: Person) : Boolean {
        val nbrRow = db!!.update(
            DbContract.PersonTable.TABLE_NAME,
            getContentValues(person),
            DbContract.PersonTable.ID + " = ? ",
            arrayOf(person.id.toString())
        )
        return nbrRow == 1
    }

    // Méthode delete
    // -> qui aura en paramètre l'ID de la Person
    // -> qui retournera un Boolean
    fun delete(personId: Long) : Boolean {
        val nbrRow = db!!.delete(
            DbContract.PersonTable.TABLE_NAME,
            DbContract.PersonTable.ID + " = ? ",
            arrayOf(personId.toString())
        )
        return nbrRow == 1
    }
    // endregion
}
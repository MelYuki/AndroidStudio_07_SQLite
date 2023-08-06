package be.melyuki.demo07_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import be.melyuki.demo07_sqlite.database.dao.PersonDao
import be.melyuki.demo07_sqlite.models.Person
import java.time.LocalDate

// region DEV CHRONOLOGY:
// - Data Class -> Person
// - Class -> DbContract
// - Class -> DbHelper
// - Class -> PersonDao
// - Test methode -> MainActivity
// endregion

class MainActivity : AppCompatActivity() {

    // Créa d'un TAG générique de débug
    private val TAG = "Debug"
    // Import du DAO
    private val personDao = PersonDao(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ouverture de l'écriture
        personDao.openWritable()

        // Ajouter des Person dans la DB
//        addPerson()

        // Afficher les Person de la DB
        getPerson()

        // Supprimer une Person
        removePerson()

        // Afficher les Person de la DB
        getPerson()

        // Afficher par l'id
        getId(1)
        getId(3)
    }

    private fun getId(id : Long) {
        val person : Person? = personDao.getById(id)

        // Log.w(TAG, if (person != null) person.toString() else "Aucun élément trouvé")
        // Replace with "elvis expression"
        Log.w(TAG, person?.toString() ?: "Aucun élément trouvé")
    }

    private fun getPerson() {
        val people : List<Person> = personDao.getAll()

        for (person in people) {
            Log.w(TAG, person.toString())
        }
    }

    private fun removePerson() {
        val personDel : Boolean = personDao.delete(4)

        Log.w(TAG, if (personDel) "Élément supprimé" else "Aucun élément à supprimer")
    }

    private fun addPerson() {
        val luffy : Person = Person(
            firstname = "Luffy",
            lastname =  "Monkey D.",
            birthDate = LocalDate.of(1997, 5, 5),
            email =  "luffy@demo.be",
            phone = null
        )

        val zoro : Person = Person(
            firstname = "Zoro",
            lastname =  "Roronoa",
            birthDate = LocalDate.of(1997, 11, 11),
            email =  "zoro@demo.org",
            phone = null
        )

        val sanji : Person = Person(
            firstname = "Sanji",
            lastname =  "Vinsmoke",
            birthDate = LocalDate.of(1997, 3, 2),
            email =  "sanji@demo.com",
            phone = null
        )

        val chopper : Person = Person(
            firstname = "Chopper",
            lastname =  "Tony Tony",
            birthDate = LocalDate.of(1997, 12, 24),
            email =  "chopper@demo.net",
            phone = null
        )

        personDao.create(luffy)
        personDao.create(zoro)
        personDao.create(sanji)
        personDao.create(chopper)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Fermeture de la connexion à la DB
        personDao.close()
    }
}
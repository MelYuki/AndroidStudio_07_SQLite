package be.melyuki.demo07_sqlite.database

class DbContract {
    // Infos de la DB
    companion object{
        const val NAME : String = "database.sqlite"
        const val VERSION : Int = 1
    }

    // Infos nécessaire pour la table "person" de la DB
    class PersonTable {

        companion object{
            // Le nom de la table
            const val TABLE_NAME = "person"

            // Les noms des colonnes
            const val ID = "_id"
            const val FIRST_NAME = "first_name"
            const val LAST_NAME = "last_name"
            const val BIRTH_DATE = "birth_date"
            const val EMAIL = "email"
            const val PHONE_NBR = "phone_nbr"

            // Script pour générer la table
            const val SCRIPT_CREATE =
                "CREATE TABLE $TABLE_NAME (" +
                    "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$FIRST_NAME VARCHAR(50), " +
                    "$LAST_NAME VARCHAR(50), " +
                    "$BIRTH_DATE VARCHAR(50), " +
                    "$EMAIL VARCHAR(50), " +
                    "$PHONE_NBR VARCHAR(50) " +
                ")"

            // Script pour éffacer la table
            const val SCRIPT_DROP =
                "DROP TABLE $TABLE_NAME"
        }
    }
}
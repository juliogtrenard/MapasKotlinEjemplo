package es.icjardin.mapas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Clase de gestión de la base de datos
 */

class MarcadorDatabaseHelper (context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val   DATABASE_NAME = "marcadores.db"
        private const val   DATABASE_VERSION = 1
        private const val   TABLE_NAME = "marcadores"
        // y ahora las columnas o atributos
        private const val   COLUMN_ID = "id"
        private const val   COLUMN_NOMBRE = "nombre"
        private const val   COLUMN_LATITUD = "latitud"
        private const val   COLUMN_LONGITUD = "longitud"

    }

    /**
     * Creadora de la tabla
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY, " +
                    "$COLUMN_NOMBRE TEXT, " +
                    "$COLUMN_LATITUD REAL, " +
                    "$COLUMN_LONGITUD REAL)"
        db?.execSQL(createTableQuery)
    }

    /**
     * Modifica la tabla de la base de datos
     */

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery =
            "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        // ya hemos mirado si existia o no, ahora la podemos crear sin que de errores
        onCreate(db)
    }

    /**
     * Pasado un objeto marcador, lo añade a la base de datos
     * @param marcador el marcador a añadir
     */

    fun insertMarcador(marcador: Marcador){
        //la abro en modo escritura
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE,       marcador.nombre)
            put(COLUMN_LATITUD,       marcador.latitud)
            put(COLUMN_LONGITUD, marcador.longitud)
            //en este caso ID es autonumérico
        }
        //insertamos datos
        db.insert(TABLE_NAME, null, values)
        //y ahora a cerrar
        db.close()
    }

    /**
     * Lee la base de datos y rellena una List de tipo Nota
     */

    fun getAllMarcadores() : List<Marcador> {
        //creo una lista mutable para poder cambiar cosas
        val listaMarcadores = mutableListOf<Marcador>()

        //la abro en modo lectura
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME"

        //lanza un cursor
        val cursor = db.rawQuery(query, null)

        //itera mientras que exista otro
        while (cursor.moveToNext()){
            val id =            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nombre =        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            val latitud =        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD))
            val longitud =   cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD))
            //creamos un objeto temporal de tipo Marcador
            val marcador =  Marcador(id, nombre, latitud, longitud)
            //añadimos la nota
            listaMarcadores.add(marcador)
        }
        //cerrar las conexiones
        cursor.close()
        db.close()

        return listaMarcadores
    }



    /**
     * Recibe un integer que es su posición en la lista, con esto recuperara los datos del marcador
     * @return Un marcador en función del integer que es su posicion
     * @param idMarcador el número de la posición del marcador
     */

    fun getIdMarcador(idMarcador : Int) : Marcador {
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$idMarcador"

        //lanza un cursor
        val cursor = db.rawQuery(query, null)
        //ve al primer registro que cumpla esa condicion (esperemos que el único)
        cursor.moveToFirst()

        //leo los datos de la consulta
        val id =            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nombre =        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
        val latitud =        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD))
        val longitud =   cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD))

        //cierro conexiones y devuelvo el marcador
        cursor.close()
        db.close()
        return Marcador(id, nombre, latitud, longitud)
    }


    /**
     * Le pasamos un marcador y actualiza su contenido, tiene una sintaxis especial, no lo hace con un update de sql normal
     */

    fun updateMarcador(marcador: Marcador) {
        val db = writableDatabase
        // creamos los valores a cambiar
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, marcador.nombre)
            put(COLUMN_LATITUD, marcador.latitud)
            put(COLUMN_LONGITUD, marcador.longitud)
        }

        // parametro con las condiciones a cumplir
        val whereClausula   = "$COLUMN_ID = ?"
        //prametro con la lista de elmentos a buscar
        val whereArgs       = arrayOf(marcador.id.toString())

        //ejecución de esta tabla con esos valores en esa clausula con esas condiciones
        db.update(TABLE_NAME, values, whereClausula, whereArgs)

        db.close()
    }


    /**
     * Elimina un marcador dado su id
     */
    fun deleteMarcador (idMarcador : Int) {
        val db = writableDatabase

        val whereClauses    = "$COLUMN_ID = ?"
        val whereArgs       = arrayOf(idMarcador.toString())
        //eliminar el objeto
        db.delete(TABLE_NAME,whereClauses,whereArgs)

        db.close()
    }

}
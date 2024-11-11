package es.icjardin.mapas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Marcador geográfico con sus atributos.
 *
 * @property id ID del marcador.
 * @property latitud Latitud del marcador.
 * @property longitud Longitud del marcador.
 * @property titulo Título del marcador.
 * @property descripcion Descripción del marcador.
 */
data class Marcador(
    val id: Long,
    val latitud: Double,
    val longitud: Double,
    val titulo: String,
    val descripcion: String
)

/**
 * Clase de gestión de la base de datos
 */
class MarcadorDatabaseHelper (context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "marcadores.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_MAP = "Marcadores"
        // y ahora las columnas o atributos
        private const val COLUMN_ID = "Id"
        private const val COLUMN_TITULO="Ubicacion"
        private const val COLUMN_LATITUDE="Latitud"
        private const val COLUMN_LONGITUDE="Longitud"
        private const val COLUMN_DESCRIPTION="Descripcion"
    }

    /**
     * Lista de ciudades para insertar en la base de datos.
     */
    public val ciudades: List<Marcador> = listOf(
        Marcador(0,42.849998, -2.683333,"Vitoria-Gasteiz","Capital del País Vasco"),
        Marcador(0,40.416775, -3.703790,"Madrid","Capital de España"),
        Marcador(0,41.3784, 2.1925,"Barcelona","Ciudad portuaria en la costa noreste de España"),
        Marcador(0,39.469907, -0.376288,"Valencia","Conocida por su Ciudad de las Artes y las Ciencias, sus playas y como cuna de la paella."),
        Marcador(0,37.388630, -5.982329,"Sevilla","La capital de Andalucía."),
        Marcador(0,37.177336, -3.598557,"Granada","Ciudad histórica conocida por la Alhambra."),
        Marcador(0,43.263012, -2.934685,"Bilbao","En el País Vasco, es conocida por el Museo Guggenheim."),
        Marcador(0,40.963374, -5.663540,"Salamanca","Famosa por su Universidad, una de las más antiguas de Europa."),
        Marcador(0,41.648823, -0.889207,"Zaragoza"," Capital de Aragón, conocida por la Basílica del Pilar."),
        Marcador(0,36.721276, -4.421321,"Malaga","Ciudad en la Costa del Sol, famosa por sus playas, el Museo Picasso.")
    )

    /**
     * Creadora de la tabla
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_MAP (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_TITULO TEXT," +
                "$COLUMN_LATITUDE DOUBLE," +
                "$COLUMN_LONGITUDE DOUBLE," +
                "$COLUMN_DESCRIPTION TEXT)"
        db.execSQL(createTable)
    }

    /**
     * Modifica la tabla de la base de datos
     *
     * @param db La base de datos a actualizar.
     * @param oldVersion La versión anterior de la base de datos.
     * @param newVersion La nueva versión de la base de datos.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MAP")
        onCreate(db)
    }

    /**
     * Inserta un nuevo marcador en la base de datos.
     * @param marcador El marcador a insertar.
     */
    fun insertar(marcador: Marcador) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put(COLUMN_TITULO, marcador.titulo)
                put(COLUMN_DESCRIPTION, marcador.descripcion)
                put(COLUMN_LATITUDE, marcador.latitud)
                put(COLUMN_LONGITUDE, marcador.longitud)
            }
            db.insert(TABLE_MAP, null, values)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
     * Devuelve todos los marcadores de la base de datos.
     * @return Una lista mutable de marcadores.
     */
    fun getMarcadores(): MutableList<Marcador> {
        val marcadores = mutableListOf<Marcador>()
        val db = readableDatabase
        val cursor = db.query(TABLE_MAP, arrayOf(COLUMN_ID, COLUMN_TITULO, COLUMN_LATITUDE,
            COLUMN_LONGITUDE, COLUMN_DESCRIPTION), null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val titulo=getString(getColumnIndexOrThrow(COLUMN_TITULO))
                val latitud=getDouble(getColumnIndexOrThrow(COLUMN_LATITUDE))
                val longitud=getDouble(getColumnIndexOrThrow(COLUMN_LONGITUDE))
                val descripcion=getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                marcadores.add(Marcador(id, latitud,longitud,titulo,descripcion))
            }
            close()
        }
        return marcadores
    }

    /**
     * Devuelve un marcador específico por su título.
     * @param titulo El título del marcador a buscar.
     * @return El marcador encontrado, o null si no existe.
     */
    fun getMarcadorByTitulo(titulo: String): Marcador? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MAP,
            arrayOf(COLUMN_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_TITULO, COLUMN_DESCRIPTION),
            "$COLUMN_TITULO = ?",
            arrayOf(titulo),
            null, null, null
        )

        var marcador: Marcador? = null
        with(cursor) {
            if (moveToFirst()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val latitud = getDouble(getColumnIndexOrThrow(COLUMN_LATITUDE))
                val longitud = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUDE))
                val descripcion = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                marcador = Marcador(id, latitud, longitud, titulo, descripcion)
            }
            close()
        }
        return marcador
    }
}
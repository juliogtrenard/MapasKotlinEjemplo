package es.icjardin.mapas

/**
 * como va a ser una clase para guardar datos la precedemos por data,
 * @param id Identificador
 * @param nombre El nombre de la ubicacion del marcador
 * @param latitud la latitud de la ubicacion del marcador
 * @param longitud la longitud de la ubicacion del marcador
 */
data class Marcador (val id : Int, val nombre : String , val latitud : Double, val longitud:Double)
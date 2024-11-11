package es.icjardin.mapas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.icjardin.mapas.databinding.ActivityMapsBinding

/**
 * Activity principal para mostrar un mapa con los marcadores obtenidos desde la base de datos.
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var dbHelper: MarcadorDatabaseHelper

    /**
     * Lista de marcadores de la base de datos.
     * */
    var marcadores = mutableListOf<Marcador>()

    /**
     * Inicializa la actividad y configura el mapa.
     * @param savedInstanceState estado de la actividad previamente guardado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = MarcadorDatabaseHelper(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        marcadores = dbHelper.getMarcadores()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Método llamado cuando el mapa está listo para ser manipulado.
     * Agrega los marcadores de la base de datos al mapa y configura el comportamiento de los clics en los marcadores.
     * @param googleMap instancia lista para ser manipulada.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        for(ciudad in dbHelper.ciudades){
            dbHelper.insertar(ciudad)
        }

        val puntos:List<Marcador> = dbHelper.getMarcadores()
        for(punto in puntos){
            val lugar=LatLng(punto.latitud,punto.longitud)
            mMap.addMarker(MarkerOptions().position(lugar).title(punto.titulo))
        }

        mMap.setOnMarkerClickListener { marker ->
            marker.showInfoWindow() // Mostrar la ventana de información
            true
        }

        val centro = LatLng(40.416775, -3.703790)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(centro))

        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this, MarcadorActivity::class.java) // Iniciar la actividad
            intent.putExtra("point", marker.title) // Pasar el título del marcador
            startActivity(intent) // Lanzar la actividad
        }
    }
}
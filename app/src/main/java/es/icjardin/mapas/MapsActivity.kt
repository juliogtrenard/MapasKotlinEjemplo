package es.icjardin.mapas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.icjardin.mapas.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    /**
     * el enlace con la base de datos, la necesitaremos para rellenar el comienzo
     */
    private lateinit var db : MarcadorDatabaseHelper

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicializar la base de datos
        db = MarcadorDatabaseHelper(this)

        //Guarda los 10 marcadores en la BBDD
        //val madrid:Marcador = Marcador(0, "Madrid", 40.4165, -3.70256)
        //guardarMarcador(madrid.nombre, madrid.latitud, madrid.longitud)
        //val barcelona:Marcador = Marcador(0, "Barcelona", 41.3887900, 2.1589900)
        //guardarMarcador(barcelona.nombre, barcelona.latitud, barcelona.longitud)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val vitoria = LatLng(42.84998, -2.67268)
        mMap.addMarker(MarkerOptions().position(vitoria).title("Marcador en Vitoria"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vitoria))

        // Leer los marcadores de la BBDD
        //val listaMarcadores = db.getAllMarcadores()

        // Añadir marcadores
        /*for (marcador in listaMarcadores) {
            val m = LatLng(marcador.latitud, marcador.longitud)
            mMap.addMarker(MarkerOptions().position(m).title("Marcador en " + marcador.nombre))
        }*/
    }

    /**
     * Asegura que los datos pasados sean validos para guardarse
     */
    private fun guardarMarcador(nombre: String, latitud: Double, longitud:Double){
        val marcador = Marcador(0, nombre, latitud, longitud)
        //lo inserto
        db.insertMarcador(marcador)
    }
}
package es.icjardin.mapas

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad que muestra la información de un marcador específico.
 */
class MarcadorActivity : AppCompatActivity() {
    /**
     * Inicia la interfaz y muestra los datos del marcador en la base de datos.
     * @param savedInstanceState estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcador)

        // Obtener el título del marcador desde el Intent
        val titulo = intent.getStringExtra("point") ?: return

        // Inicializa la base de datos para obtener el marcador
        val dbHelper = MarcadorDatabaseHelper(this)
        val marcador = dbHelper.getMarcadorByTitulo(titulo)

        // Actualiza la interfaz de usuario con sus datos
        marcador?.let {
            // Muestra el título y la descripción
            findViewById<TextView>(R.id.tvTitulo).text = it.titulo
            findViewById<TextView>(R.id.tvDescripcion).text = it.descripcion

            // Muestra la imagen
            val imageResource = resources.getIdentifier(it.titulo.lowercase(), "raw", packageName)
            if (imageResource != 0) { // Verifica si la imagen existe
                findViewById<ImageView>(R.id.marcadorImageView).setImageResource(imageResource)
            }
        }

        // Referencia al botón
        val botonRegresar = findViewById<Button>(R.id.boton_regresar)

        // Configurar el evento de clic del botón
        botonRegresar.setOnClickListener {
            // Cierra la actividad actual y regresa a la actividad anterior
            finish()
        }
    }
}
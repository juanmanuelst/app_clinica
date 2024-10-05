package com.upn.app_citaclinica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AnularCitaActivity : AppCompatActivity() {

    private lateinit var editIdCita: EditText
    private lateinit var imgbBuscar: ImageButton
    private lateinit var txtUsuarioCita: TextView
    private lateinit var txtEspecialidadCita: TextView
    private lateinit var txtDoctorCita: TextView
    private lateinit var txtFechaCita: TextView
    private lateinit var txtHorarioCita: TextView
    private lateinit var btnCancela:Button
    private lateinit var btnAnularCita:Button

    /*   Datos Usuarios   */
    private var nombres:String =""
    private var apellidos:String=""
    private var idusuario:Int=0

    private var idCita: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_citas_anular)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asinarReferencias()
        cancelarcita()
        anularCita()
        recibirDatos()
    }

    private fun asinarReferencias(){
        editIdCita = findViewById(R.id.editIdCita)
        imgbBuscar = findViewById(R.id.imgbBuscar)
        txtUsuarioCita = findViewById(R.id.txtUsuarioCita)
        txtEspecialidadCita = findViewById(R.id.txtEspecialidadCita)
        txtDoctorCita = findViewById(R.id.txtDoctorCita)
        txtFechaCita = findViewById(R.id.txtFechaCita)
        txtHorarioCita = findViewById(R.id.txtHorarioCita)
        btnCancela=findViewById(R.id.btnCancela)
        btnAnularCita=findViewById(R.id.btnAnularCita)

        // lógica de búsqueda al botón
        imgbBuscar.setOnClickListener {
            val idCitaStr = editIdCita.text.toString()

            // Verificar si el campo de búsqueda no está vacío
            if (idCitaStr.isNotEmpty()) {
                val idCita = idCitaStr.toIntOrNull()
                if (idCita != null) {
                    buscarCita(idCita)
                } else {
                    Toast.makeText(this, "Ingrese un ID de cita válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese el ID de la cita", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Función para buscar la cita
    private fun buscarCita(idCita: Int) {
        // Hacer la solicitud dentro de una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Llamada a Retrofit para obtener una cita de tipo CitaMedica
                val response = RetrofitCliente.webService.buscarCitaPorId(idCita)
                if (response.isSuccessful) {
                    val citaMedica = response.body()  // Ahora 'response.body()' devuelve un CitaMedica
                    citaMedica?.let {
                        // Muestra los datos de la cita en los TextViews
                        runOnUiThread {
                            txtUsuarioCita.text = citaMedica.UsuarioCita
                            txtEspecialidadCita.text = citaMedica.Especialidad
                            txtDoctorCita.text = citaMedica.Medico
                            txtFechaCita.text = formatearFecha(citaMedica.FechaCita ?: " ")
                            txtHorarioCita.text = citaMedica.HoraCita
                        }
                    } ?: runOnUiThread {
                        Toast.makeText(this@AnularCitaActivity,"Cita no encontrada",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AnularCitaActivity,"Error en la búsqueda de la cita",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@AnularCitaActivity,"Error en la búsqueda de la cita",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun formatearFecha(fechaStr: String): String {
        // Primero, parseamos la fecha en formato ISO 8601
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) //  formato de fecha

        return try {
            val fecha: Date = formatoEntrada.parse(fechaStr)!!
            formatoSalida.format(fecha)
        } catch (e: Exception) {
            e.printStackTrace()
            "Fecha no válida"
        }
    }


    private fun anularCita() {
        btnAnularCita.setOnClickListener {
            val idCitaStr = editIdCita.text.toString()
            if (idCitaStr.isNotEmpty()) {
               // val idCita = idCitaStr.toIntOrNull()
                val idCita = idCitaStr.replace("C", "").toIntOrNull()
                if (idCita != null) {
                    // Mostrar la confirmación antes de proceder con la anulación
                    mostrarConfirmacionAnulacion(idCita)
                } else {
                    Toast.makeText(this, "Ingrese un ID de cita válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese el ID de la cita", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarConfirmacionAnulacion(idCita: Int) {
        // Crear el AlertDialog para la confirmación
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmar Cancelar Cita")
        builder.setMessage("¿Está seguro que desea cancelar la cita?")

        // Opción "Sí"
        builder.setPositiveButton("Sí") { dialog, which ->
            // Proceder con la anulación de la cita
            validacionanularCita(idCita)
        }

        // Opción "No"
        builder.setNegativeButton("No") { dialog, which ->
            // Cerrar el diálogo y no hacer nada
            dialog.dismiss()
        }

        // Mostrar el diálogo
        val dialog = builder.create()
        dialog.show()
    }

    private fun validacionanularCita(idCita: Int) {
        // Hacer la solicitud dentro de una corrutina
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitCliente.webService.anularCita(idCita)
                if (response.isSuccessful) {
                    val anulacionResponse = response.body()
                    runOnUiThread {
                        Toast.makeText(this@AnularCitaActivity, anulacionResponse?.mensaje ?: "Cita anulada correctamente", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }
                } else {
                    Log.e("CitasAnularActivity", "Error: ${response.errorBody()?.string()}")
                    runOnUiThread {
                        Toast.makeText(this@AnularCitaActivity, "Error al anular la cita", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("CitasAnularActivity", "Error en la anulación de la cita: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AnularCitaActivity, "Error en la anulación de la cita", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun actualizarHorario(id_horario: Int){
        Log.e("****************************", "ENTRA ACTUALIZAR horario")
        Log.e("****************************", id_horario.toString())
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.actualizarHorario(id_horario)
            withContext(Dispatchers.Main) {
                if (rpta.isSuccessful) {
                    Log.e("Exito", "Se actualizó horario")
                } else {
                    Log.e("Error", "No se pudo actualizar el horario")
                }
            }
        }
    }


    private fun limpiarCampos() {
        editIdCita.text.clear()
        txtUsuarioCita.text = ""
        txtEspecialidadCita.text = ""
        txtDoctorCita.text = ""
        txtFechaCita.text = ""
        txtHorarioCita.text = ""
    }

    private fun cancelarcita(){
        btnCancela.setOnClickListener {
            val intent=Intent(this,PrincipalActivity::class.java)
            intent.putExtra("var_nombres",nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        }
    }
    private fun recibirDatos(){
        nombres = (intent.getStringExtra("var_nombres")).toString()
        apellidos = (intent.getStringExtra("var_apellidos")).toString()
        idusuario = intent.getIntExtra("var_usuario",0)
    }



}
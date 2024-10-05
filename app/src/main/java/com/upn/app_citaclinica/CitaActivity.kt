package com.upn.app_citaclinica

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CitaActivity : AppCompatActivity() {

    private lateinit var spEspecialidad:Spinner
    private var especialidades: List<Especialidad> = listOf()
    private var horarios: List<Horario> = listOf()
    private lateinit var esp:Especialidad
    private lateinit var txtFecha:EditText
    private lateinit var txtHorario:TextView
    private var fech:String = "2024/09/30"
    private lateinit var radioGroup: RadioGroup
    private lateinit var btnCancelar:Button
    private lateinit var btnReservar:Button
    private var ok:Boolean = false

    /*   Datos Usuarios   */
    private var nombres:String =""
    private var apellidos:String=""
    private var idusuario:Int=0

    /*Variables para registrar en BD*/
    private var cita_fecha:String =""
    private var cita_hora:String =""
    private var cita_idMedico:Int = 0
    private var id_horario:Int=0
    /********************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cita)
        asignarReferencias()
        recibirDatos()

    }

    private fun asignarReferencias() {
        spEspecialidad = findViewById(R.id.spEspecialidad)
        txtFecha = findViewById(R.id.txtFecha)
        radioGroup = findViewById(R.id.radioGroup)
        btnCancelar= findViewById(R.id.btnCancelar)
        btnReservar= findViewById(R.id.btnReservar)

        obtenerEspecialidades()
        txtFecha.setOnClickListener { showDatePickerDialog() }
        btnCancelar.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.putExtra("var_nombres",nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        }
        btnReservar.setOnClickListener {
            registrarCita()
        }
    }
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        txtFecha.setText(String.format("%02d", day)+"/"+String.format("%02d", month+1)+"/"+year.toString())

        fech = "$year-"+(month+1)+"-$day"
        obtenerHorarios()
    }


    private fun obtenerEspecialidades() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerEspecialidades()
            if (rpta.isSuccessful) {
                val body = rpta.body()
                if (body != null) {
                    especialidades = body.listaEspecialidades
                    withContext(Dispatchers.Main) {
                        llenarSpinner()
                    }
                }
            }
        }
    }

    private fun llenarSpinner() {
        val especialidad = especialidades.map { it.especialidad_nombre }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, especialidad)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEspecialidad.adapter = adapter


        spEspecialidad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val especialidadSeleccionada = especialidades[position]
                esp = especialidadSeleccionada
                obtenerHorarios()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun obtenerHorarios() {
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.obtenerHorarios(fech+"&"+esp.id_especialidad)
            if (rpta.isSuccessful) {
                val horarioResponse = rpta.body()
                if (horarioResponse != null && !horarioResponse.listaHorarios.isNullOrEmpty()) {
                    horarios = horarioResponse.listaHorarios
                    withContext(Dispatchers.Main) {
                        llenarRadioGroup()
                    }
                } else {
                    horarios = listOf()
                    withContext(Dispatchers.Main) {
                        llenarRadioGroup()
                    }
                    Log.e("VACIOO", "VACIOO.")
                    Log.d("HORARIOS", "Respuesta vacía o lista de horarios vacía")
                }
            } else {
                Log.e("Error", "Error al obtener horarios: ${rpta.errorBody()?.string()}")
            }
        }
    }

    private fun llenarRadioGroup() {
        radioGroup.removeAllViews()

        if (horarios.isEmpty()) {

            val radioButton = RadioButton(this)
            radioButton.text = "No hay horarios disponibles"
            radioButton.isEnabled = false
            radioGroup.addView(radioButton)
        } else {
            for (horario in horarios) {
                val radioButton = RadioButton(this)
                radioButton.id = View.generateViewId()
                radioButton.text = horario.horario_horas+" / "+ "Dr. "+horario.medico_nombre+" "+horario.medico_apellido
                radioButton.tag = horario.id_horario
                radioGroup.addView(radioButton)
            }

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val radioButtonSeleccionado = findViewById<RadioButton>(checkedId)
                val horarioIdSeleccionado = radioButtonSeleccionado.tag as Int?

                if (horarioIdSeleccionado != null) {
                    val horarioSeleccionado = horarios.firstOrNull { it.id_horario == horarioIdSeleccionado }
                    if (horarioSeleccionado != null) {
                        cita_fecha = horarioSeleccionado.horario_fecha
                        cita_hora = horarioSeleccionado.horario_horas
                        cita_idMedico = horarioSeleccionado.id_medico
                        id_horario = horarioSeleccionado.id_horario
                    }
                } else {
                    Log.e("Error", "No se pudo obtener el horario seleccionado.")
                }
            }
        }
    }

    private fun formatearFecha(fecha: String): String {    val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formato.timeZone = TimeZone.getTimeZone("UTC")

        val formatoMysql = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val fecha = formato.parse(fecha)
        return formatoMysql.format(fecha)
    }


    private fun registrarCita(){

        val id_usuario = idusuario
        val id_medico = cita_idMedico
        val cita_fecha = formatearFecha(cita_fecha)
        val cita_hora = cita_hora


        val cita = Cita(0, id_usuario, id_medico, cita_fecha, cita_hora,1)

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.agregarCita(cita)
            withContext(Dispatchers.Main) {
                if (rpta.isSuccessful) {
                    actualizarHorario(id_horario)
                    mostrartMensaje(rpta.body().toString())

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

    private fun mostrartMensaje(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Informacion")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.putExtra("var_nombres",nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        })
        ventana.create().show()
    }

    private fun recibirDatos(){
        nombres = (intent.getStringExtra("var_nombres")).toString()
        apellidos = (intent.getStringExtra("var_apellidos")).toString()
        idusuario = intent.getIntExtra("var_usuario",0)
    }
}
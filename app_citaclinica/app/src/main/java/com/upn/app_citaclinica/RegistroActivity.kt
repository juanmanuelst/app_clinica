package com.upn.app_citaclinica

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.KeyStore.PasswordProtection

class RegistroActivity : AppCompatActivity() {

    private lateinit var txtdni: EditText
    private lateinit var txtNombres: EditText
    private lateinit var txtApellidos: EditText
    private lateinit var editTxtCorreo: EditText
    private lateinit var editTxtPassword: EditText
    private lateinit var btnRegistrarCuenta: Button
    private lateinit var btnRegresarInicio: Button

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        enableEdgeToEdge()
        asignarReferencias()
    }

    private fun asignarReferencias() {
        txtdni = findViewById(R.id.txtdni)
        txtNombres = findViewById(R.id.txtNombres)
        txtApellidos = findViewById(R.id.txtApellidos)
        editTxtCorreo = findViewById(R.id.editTxtCorreo)
        editTxtPassword = findViewById(R.id.editTxtPassword)
        btnRegistrarCuenta = findViewById(R.id.btnRegistrarCuenta)
        btnRegresarInicio = findViewById(R.id.btnRegresarInicio)
        btnRegistrarCuenta.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario(){
        val dni = txtdni.text.toString()
        val nombres = txtNombres.text.toString()
        val apellidos = txtApellidos.text.toString()
        val correo = editTxtCorreo.text.toString()
        val password = editTxtPassword.text.toString()

        Log.d("===","dni: "+dni)
        Log.d("===","nombres: "+dni)
        Log.d("===","apellidos: "+dni)
        Log.d("===","correo: "+dni)
        Log.d("===","password: "+dni)

        val usuario = Usuario(0, dni, nombres, apellidos, correo, password)

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitCliente.webService.agregarUsuario(usuario)
            runOnUiThread {
                if (rpta.isSuccessful) {
                    mostrartMensaje(rpta.body().toString())
                }
            }
        }
    }

    private fun mostrartMensaje(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Informacion")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}
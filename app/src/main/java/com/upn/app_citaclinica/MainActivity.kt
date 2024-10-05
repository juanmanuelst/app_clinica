package com.upn.app_citaclinica

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var btnInicioRegistrar:Button
    private lateinit var btnInicio:Button
    private lateinit var txtCorreo:EditText
    private lateinit var txtPassword:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        asignarReferencias()
    }

    private fun asignarReferencias() {
        btnInicioRegistrar = findViewById(R.id.btnInicioRegistrar)
        btnInicio = findViewById(R.id.btnInicio)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtPassword = findViewById(R.id.txtPassword)

        btnInicioRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnInicio.setOnClickListener {

            val correo = txtCorreo.text.toString()
            val pass = txtPassword.text.toString()

            if (correo == "" || pass == "" ){
                mostrartMensaje("Ingrese Correo y/o Password")
                //Toast.makeText(baseContext,"Ingrese Correo y/o Password", Toast.LENGTH_LONG).show()
            }else{
                Log.d("===", "txtCorreo: "+txtCorreo.text.toString())
                Log.d("===", "txtPassword: "+txtPassword.text.toString())

                CoroutineScope(Dispatchers.IO).launch {
                    val rpta = RetrofitCliente.webService.buscarUsuarioxCorreo(correo)
                    Log.d("===", "rpta: "+rpta)
                    runOnUiThread {
                        if(rpta.isSuccessful){
                            val usuario = rpta.body()
                            Log.d("===", "usuario: "+usuario)
                            if(usuario != null && usuario.usuario_correo == correo && usuario.usuario_contrasena == pass){
                                val intent = Intent(baseContext, PrincipalActivity::class.java)
                                intent.putExtra("var_nombres",usuario.usuario_nombre)
                                intent.putExtra("var_apellidos",usuario.usuario_apellido)
                                intent.putExtra("var_usuario",usuario.id_usuario)
                                startActivity(intent)
                            }else{
                                mostrartMensaje("Usuario/Password incorrectos")
                            //Toast.makeText(baseContext,"Usuario/Password incorrectos", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }


        }
    }

    private fun mostrartMensaje(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Informacion")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
        })
        ventana.create().show()
    }
}
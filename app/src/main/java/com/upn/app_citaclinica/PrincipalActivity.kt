package com.upn.app_citaclinica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PrincipalActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextView
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        asignarReferencias()
        recibirDatos()
    }

    private fun asignarReferencias(){
        txtUsuario = findViewById(R.id.txtUsuario)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun recibirDatos(){
        val nombres = intent.getStringExtra("var_nombres")
        val apellidos = intent.getStringExtra("var_apellidos")
        txtUsuario.text = nombres+" "+apellidos
    }
}
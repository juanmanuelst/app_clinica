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
    private lateinit var btnAgendarCita: Button
    private lateinit var btnInfo: Button
    private lateinit var btnCitas: Button
    private lateinit var btnCancelarCita:Button

    /*datos*/
    private var nombres:String =""
    private var apellidos:String=""
    private var idusuario:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        asignarReferencias()
        recibirDatos()
    }

    private fun asignarReferencias(){
        txtUsuario = findViewById(R.id.txtUsuario)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnAgendarCita = findViewById(R.id.btnAgendarCita)
        btnInfo = findViewById(R.id.btnInfo)
        btnCitas= findViewById(R.id.btnCitas)
        btnCancelarCita= findViewById(R.id.btnCancelarCita)

        btnAgendarCita.setOnClickListener {
            val intent = Intent(baseContext,CitaActivity::class.java)
            intent.putExtra("var_nombres", nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        }
        btnCerrarSesion.setOnClickListener {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
        btnInfo.setOnClickListener {
            val intent = Intent(baseContext, InfoActivity::class.java)
            intent.putExtra("var_nombres", nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        }
        btnCitas.setOnClickListener {
            val intent = Intent(baseContext, MisCitasActivity::class.java)
            intent.putExtra("var_nombres",nombres)
            intent.putExtra("var_apellidos",apellidos)
            intent.putExtra("var_usuario",idusuario)
            startActivity(intent)
        }

        btnCancelarCita.setOnClickListener {
            val intent = Intent(baseContext, AnularCitaActivity::class.java)
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
        //idusuario= ((intent.getStringExtra("var_usuario")).toString()).toInt()
        txtUsuario.text = "Bienvenido "+nombres+" "+apellidos
    }
}
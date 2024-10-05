package com.upn.app_citaclinica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InfoActivity : AppCompatActivity() {

    private lateinit var btnVolverMenu2: Button
    private lateinit var btnMapa: Button

    /*datos*/
    private var nombres:String =""
    private var apellidos:String=""
    private var idusuario:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        asignarReferencias()
        recibirDatos()
    }
    private fun asignarReferencias() {
        btnVolverMenu2 = findViewById(R.id.btnVolverMenu2)
        btnMapa = findViewById(R.id.btnMapa)
        btnMapa.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }


        btnVolverMenu2.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.putExtra("var_nombres", nombres)
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
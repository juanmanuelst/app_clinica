package com.upn.app_citaclinica

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : FragmentActivity(),OnMapReadyCallback {

    private lateinit var map:GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        asignarReferencias()
    }

    private fun asignarReferencias(){
        val mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as  SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.uiSettings.isZoomControlsEnabled = true
        val coordenada = LatLng(-12.078222668328406, -77.04094496086326)
        val marcador = MarkerOptions().position(coordenada).title("SEDE CENTRAL")
        map.addMarker(marcador)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenada,16f))
    }
}
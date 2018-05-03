package com.example.pc.localizacion_mapas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PETICION_PERMISO_LOCALIZACION = 101; //ESTA LINEA SOLO SE PONE EN LA OPCION DOS
    private GoogleMap mMap;//CREAR OBJETO MARKER Y DECLARAR LATITUD Y LONGITUD
    private Marker marcador;
    double lat=-34;//3.4596885
    double lng=151;//-76.47965

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
    }
    public void agregarMarcador(double lat, double lng){//CREAR METODO PARA AGREGAR MARCADOR
        LatLng coordenadas=new LatLng(lat, lng);//CREAR OBJETO LAT PARA INCLUIR LATITUD Y LONGITUD
        CameraUpdate miUbicacion=CameraUpdateFactory.newLatLngZoom(coordenadas,16); //CAMERA PARA CENTRAR A LA POSICION DEL MARKER
        if(marcador!=null)marcador.remove();
        marcador=mMap.addMarker(new MarkerOptions()
        .position(coordenadas)
        .title ("Aqui Estoy")
        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        mMap.animateCamera(miUbicacion);
    }
    private void actualizarUbicacion(Location location){//CREAR METODO PARA SABER LATITUD Y LONGITUD ACTUAL
        if(location!=null){//ASIGNAMOS NUEVA LATITUD Y LONGITUD
            lat=location.getLatitude();
            lng=location.getLongitude();
            agregarMarcador(lat,lng);
        }
    }
    LocationListener locationListener=new LocationListener() {//LOCATIONLISTENER ESTA SIEMPRE ATENTO A CAMBIOS DE LOCALIDAD
        @Override //CREA AUTOMATICAMENTE METODOS ASOCIAODS A DIFERENTES EVENTOS DE LOCALIZACION
        public void onLocationChanged(Location location) {//ACTUALIZA LA POSICION
            actualizarUbicacion(location);//LLAMAMOS AL METODO ACTUALIZAR PARA REFRESCAR LA UBICACION
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    /*private void miUbicacion(){ //OPCION UNO
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);
    }*/
    private void miUbicacion(){ //OPCION DOS
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);//TOCA INICIALIZAR ARRIVA
        } else {
            LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            actualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);
        }
    }
}
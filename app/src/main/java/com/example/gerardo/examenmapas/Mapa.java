package com.example.gerardo.examenmapas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import Utilerias.ServicioTerremotos;
import Utilerias.TerremotoDto;
import Utilerias.Utilerias;

public class Mapa extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private OnMapClickListener _escuchador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setUpMapIfNeeded();
        this._escuchador = new OnMapClickListener(this, mMap);
        mMap.setOnMapClickListener(this._escuchador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LatLng[] puntos;
        switch (item.getItemId()) {
            case R.id.map_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.map_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.acerca_de:
                Intent intAcercaDe = new Intent(getApplicationContext(), AcercaDe.class);
                startActivity(intAcercaDe);
                break;
            case R.id.terremotos:
                puntos = this._escuchador.ObtenerPuntos();
                if(puntos != null) {
                    Bundle bundle = new Bundle();
                    bundle.putDouble("norte", puntos[0].latitude);
                    bundle.putDouble("sur", puntos[1].latitude);
                    bundle.putDouble("este", puntos[0].longitude);
                    bundle.putDouble("oeste", puntos[1].longitude);
                    Intent intVerTerremotos = new Intent(getApplicationContext(), VerTerremotos.class);
                    intVerTerremotos.putExtras(bundle);
                    startActivity(intVerTerremotos);
                }
                break;
            case R.id.buscar_terremotos:
                puntos = this._escuchador.ObtenerPuntos();
                if(puntos != null){
                    this.LlamarServicio(this._escuchador.ObtenerPuntos());
                }

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void LlamarServicio(LatLng[] posiciones){
        Double[] puntos = new Double[4];
        puntos[0] = posiciones[0].latitude;
        puntos[1] = posiciones[1].latitude;
        puntos[2] = posiciones[0].longitude;
        puntos[3] = posiciones[1].longitude;

        String urlStr = getString(R.string.servicio_terremoto);
        String nombreUsuario = getString(R.string.nombre_usuario);
        ServicioTerremotos servicio = new ServicioTerremotos(urlStr, nombreUsuario, this);
        servicio.execute(puntos);
        try {
            this.MostrarMapas(Utilerias.ObtenerDtos(servicio.get()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void MostrarMapas(TerremotoDto[] terremotoDtos){
        if(terremotoDtos == null){
            Toast.makeText(this, "No se encontraron resultados, intente con otras coordenadas.", Toast.LENGTH_LONG).show();
            return;
        }

        mMap.clear();
        for(TerremotoDto terremoto : terremotoDtos){
            LatLng posicion = new LatLng(terremoto.lat, terremoto.lng);
            MarkerOptions marcador = new MarkerOptions().position(posicion).title(terremoto.eqid);
            marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(marcador);
        }
    }

    class OnMapClickListener implements GoogleMap.OnMapClickListener{

        private final Activity _actividad;
        private LatLng[] puntos;
        private GoogleMap _map;

        public OnMapClickListener(Activity actividad, GoogleMap map){
            this._actividad = actividad;
            this._map = map;
            puntos = new LatLng[2];
        }

        @Override
        public void onMapClick(LatLng latLng) {
            if(puntos[0] == null){
                puntos[0] = latLng;
            }
            else
            {
                if(puntos[1] == null){
                    puntos[1] = latLng;
                }
                else{
                    puntos[0] = puntos[1];
                    puntos[1] = latLng;
                }
            }

            this.LimpiarPuntos();
            this.MostrarPuntos();
        }

        private void MostrarPuntos() {
            for(int i = 0; i < puntos.length; i++){
                if(puntos[i] != null){
                    this._map.addMarker(new MarkerOptions().position(puntos[i]).title("Punto " + String.valueOf(i)));
                }
            }
        }

        private void LimpiarPuntos() {
            this._map.clear();
        }

        public LatLng[] ObtenerPuntos(){
            if(puntos[0] == null || puntos[1] == null){
                Toast.makeText(this._actividad, "No ha seleccionado dos puntos en el mapa.", Toast.LENGTH_LONG).show();
                return null;
            }

            return puntos;
        }
    }
}

package Utilerias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gerardo on 22/02/2015.
 */
public class ServicioTerremotos extends AsyncTask<Double, Void, JSONArray> {

    private final String _servicio;
    private final Activity _actividad;
    private final String _nombreUsuario;
    private ProgressDialog _dialogo;
    private List<TerremotoDto> _terremotos;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this._dialogo.setMessage("CARGANDO ... ");
        this._dialogo.show();
    }

    public ServicioTerremotos(String servicio, String nombreUsuario, Activity actividad){
        this._servicio = servicio;
        this._nombreUsuario = nombreUsuario;
        this._actividad = actividad;
        this._dialogo = new ProgressDialog(this._actividad);
    }

    @Override
    protected void onPostExecute(JSONArray terremotoDto) {
        super.onPostExecute(terremotoDto);
        this._dialogo.dismiss();
    }

    @Override
    protected JSONArray doInBackground(Double... params) {
        HttpClient objHttpClient = new DefaultHttpClient();
        TerremotoDto[] regresar = null;
        JSONArray terremotosJSON = null;

        try{
            String url = String.format(this._servicio, params[0], params[1], params[2], params[3], this._nombreUsuario);

            HttpGet peticion = new HttpGet(url);
            peticion.setHeader("content-type", "application/json");
            HttpResponse respuesta = objHttpClient.execute(peticion);
            String resp = EntityUtils.toString(respuesta.getEntity());
            JSONObject objJSON = new JSONObject(resp);
            terremotosJSON = objJSON.getJSONArray("earthquakes");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return terremotosJSON;
    }
}

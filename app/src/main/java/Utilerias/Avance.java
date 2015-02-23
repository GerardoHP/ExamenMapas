package Utilerias;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.example.gerardo.examenmapas.Mapa;
import com.example.gerardo.examenmapas.R;

/**
 * Created by Gerardo on 21/02/2015.
 */
public class Avance extends AsyncTask<Void, Integer, Void> {

    private final Activity _actividad;
    private final ProgressDialog _dialogo;
    private int _tope;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this._dialogo.setMessage("CARGANDO ... ");
        this._dialogo.show();
    }

    public Avance(int tope, Activity actividad) {
        this._actividad = actividad;
        this._tope = tope;
        this._dialogo = new ProgressDialog(this._actividad);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intMapa = new Intent(this._actividad, Mapa.class);
        this._actividad.startActivity(intMapa);
        this._actividad.finish();
        this._dialogo.dismiss();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < this._tope; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

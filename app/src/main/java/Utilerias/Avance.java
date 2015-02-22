package Utilerias;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.example.gerardo.examenmapas.Mapa;

/**
 * Created by Gerardo on 21/02/2015.
 */
public class Avance extends AsyncTask<Void, Integer, Void> {

    private final Activity _actividad;
    private ProgressBar _pbCargar;

    public Avance(ProgressBar pgbCargar, Activity actividad){
        this._pbCargar = pgbCargar;
        this._actividad = actividad;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intMapa = new Intent(this._actividad, Mapa.class);
        this._actividad.startActivity(intMapa);
        this._actividad.finish();
    }

    @Override
    protected Void doInBackground(Void... params) {
        for(int i=0; i<5; i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            publishProgress((int)(i/5f*100));
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this._pbCargar.setProgress(values[0]);
    }
}

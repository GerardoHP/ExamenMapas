package com.example.gerardo.examenmapas;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import Utilerias.ServicioTerremotos;
import Utilerias.TerremotoDto;
import Utilerias.Utilerias;


public class VerTerremotos extends ActionBarActivity {

    ListView _ltv_terremotos;
    private ArrayAdapter<TerremotoDto> _valores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_terremotos);

        this._ltv_terremotos = (ListView)findViewById(R.id.ltv_terremotos);

        Bundle bundle = getIntent().getExtras();
        Double[] puntos = new Double[4];
        if(bundle != null){
            puntos[0] = bundle.getDouble("norte");
            puntos[1] = bundle.getDouble("sur");
            puntos[2] = bundle.getDouble("este");
            puntos[3] = bundle.getDouble("oeste");
        }

        String urlStr = getString(R.string.servicio_terremoto);
        String nombreUsuario = getString(R.string.nombre_usuario);
        ServicioTerremotos servicio = new ServicioTerremotos(urlStr, nombreUsuario, this);
        servicio.execute(puntos);

        try {
            TerremotoDto[] terremotoDto = new Utilerias().ObtenerDtos(servicio.get());
            if(terremotoDto.length > 0){
                this._valores =  new ArrayAdapter<TerremotoDto>(this, android.R.layout.simple_list_item_1);
                for(TerremotoDto terremoto : terremotoDto){
                    this._valores.add(terremoto);
                }

                this._ltv_terremotos.setAdapter(this._valores);
            }
            else{
                Toast.makeText(getApplicationContext(), "No Existen Registros", Toast.LENGTH_SHORT).show();
                this.finish();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_ver_terremotos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

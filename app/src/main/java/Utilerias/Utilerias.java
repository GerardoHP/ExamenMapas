package Utilerias;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Gerardo on 22/02/2015.
 */
public class Utilerias {
    public static TerremotoDto ObtenerDto(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, TerremotoDto.class);
    }

    public static TerremotoDto[] ObtenerDtos(JSONArray jsonArray){
        if(jsonArray == null){
            return null;
        }

        TerremotoDto[] regresar = new TerremotoDto[jsonArray.length()];
        Gson gson = new Gson();
        for (int i=0; i < jsonArray.length(); i++){
            String jsonStr = null;
            try {
                jsonStr = jsonArray.getJSONObject(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            regresar[i] = gson.fromJson(jsonStr, TerremotoDto.class);
        }

        return regresar;
    }
}

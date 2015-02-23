package Utilerias;

/**
 * Created by Gerardo on 22/02/2015.
 */
public class TerremotoDto {
    // {"datetime":"2011-03-11 04:46:23","depth":24.4,"lng":142.369,"src":"us","eqid":"c0001xgp","magnitude":8.8,"lat":38.322}
    public String datetime;
    public float depth;
    public float lng;
    public String src;
    public String eqid;
    public float magnitude;
    public float lat;

    public TerremotoDto(){
    }

    @Override
    public String toString() {
        return String.format("Fecha: %1$2s, Magnitud: %2$2s.", this.datetime, this.magnitude);
    }
}

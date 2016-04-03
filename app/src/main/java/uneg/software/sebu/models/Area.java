package uneg.software.sebu.models;

/**
 * Created by Jhonny on 7/3/2016.
 */
public class Area {
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Area(String descripcion, Double lat, Double lon, String id) {
        this.descripcion = descripcion;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    String descripcion;
    Double lat;
    Double lon;
    String id;

}

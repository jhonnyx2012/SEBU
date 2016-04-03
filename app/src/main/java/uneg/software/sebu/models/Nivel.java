package uneg.software.sebu.models;

/**
 * Created by Jhonny on 13/3/2016.
 */
public class Nivel {
    public Nivel(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    String id;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String descripcion;
}

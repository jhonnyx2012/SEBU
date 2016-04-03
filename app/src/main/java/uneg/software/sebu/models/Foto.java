package uneg.software.sebu.models;

/**
 * Created by Jhonny on 7/3/2016.
 */
public class Foto {

    public Foto(String alarma, String url, String id) {
        this.alarma = alarma;
        this.url = url;
        this.id = id;
    }

    public String getAlarma() {
        return alarma;
    }

    public void setAlarma(String alarma) {
        this.alarma = alarma;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String alarma;
    String url;
    String id;
}

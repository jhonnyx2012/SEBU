package uneg.software.sebu.models.bd;

import com.orm.SugarRecord;

/**
 * Created by Kenny Perroni on 03/04/2016.
 */
public class Telefono extends SugarRecord {


    String telefono;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Telefono(){
    }

    public Telefono(String telefono){


        this.telefono = telefono;
    }


}

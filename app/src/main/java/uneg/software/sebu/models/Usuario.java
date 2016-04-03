package uneg.software.sebu.models;

import java.util.ArrayList;

/**
 * Created by Jhonny on 28/2/2016.
 */
public class Usuario {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String nombre;
    String apellido;
    String username;
    String correo;
    String telefono;
    String id;

    public Nivel getNivel() {
        return (nivel!=null&&nivel.size()>0)?nivel.get(0):null;
    }

    public void setNivel(ArrayList<Nivel> nivel) {
        this.nivel = nivel;
    }

    public Usuario(String nombre, String apellido, String username, String correo, String telefono, ArrayList<Nivel> nivel, String id) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.correo = correo;
        this.telefono = telefono;
        this.nivel = nivel;
        this.id = id;
    }

    ArrayList<Nivel> nivel;

    public String getFullName() {
        return nombre+" "+apellido;
    }
}

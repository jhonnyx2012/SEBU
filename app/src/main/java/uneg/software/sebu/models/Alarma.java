package uneg.software.sebu.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jhonny on 7/3/2016.
 */
public class Alarma
{
        ArrayList<Usuario> usuario;
        ArrayList<Area> area;
        ArrayList<Foto> fotos;
        String descripcion;
        int status;
        String createdAt;
        String updatedAt;
        String id;

        public Alarma(ArrayList<Usuario> usuario, ArrayList<Area> area, ArrayList<Foto> fotos, String descripcion, int status, String createdAt, String updatedAt, String id) {
                this.usuario = usuario;
                this.area = area;
                this.fotos = fotos;
                this.descripcion = descripcion;
                this.status = status;
                this.createdAt = createdAt;
                this.updatedAt = updatedAt;
                this.id = id;
        }


        public Usuario getUsuario() {

                return usuario.get(0);
        }

        public void setUsuario(ArrayList<Usuario> usuario) {
                this.usuario = usuario;
        }

        public Area getArea() {
                if(area.size()==0)return null;
                return area.get(0);
        }

        public void setArea(ArrayList<Area> area) {
                this.area = area;
        }

        public ArrayList<Foto> getFotos() {
                return fotos;
        }

        public void setFotos(ArrayList<Foto> fotos) {
                this.fotos = fotos;
        }

        public String getDescripcion() {
                return descripcion;
        }

        public void setDescripcion(String descripcion) {
                this.descripcion = descripcion;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public String getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getFirstFotoUrl() {
                if(fotos.size()==0)return "";
                return fotos.get(0).getUrl();
        }

        public String getFechaCreacion()
        {
               return getDateInFormat(createdAt);
        }

        private String getDateInFormat(String date) {
                String formattedTime="";
                try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        SimpleDateFormat output = new SimpleDateFormat("'el 'dd/MM/yyyy ' a las ' hh:mmaa");
                        Date d  = sdf.parse(date);
                        formattedTime= output.format(d);

                } catch (ParseException e) {
                        e.printStackTrace();
                }
                return formattedTime;
        }

        public ArrayList<String> getFotosStringArray() {
                ArrayList<String> aux=new ArrayList<>();
                for(Foto foto:fotos)
                {
                        aux.add(foto.getUrl());
                }

                return aux;
        }


}
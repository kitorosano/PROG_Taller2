package taller2.DTOs;

import java.io.Serializable;

public class PlataformaDTO implements Serializable {
    private String nombre;
    private String descripcion;
    private String url;

    public PlataformaDTO(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

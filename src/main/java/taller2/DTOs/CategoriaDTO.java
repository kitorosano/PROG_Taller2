package taller2.DTOs;

import java.io.Serializable;

public class CategoriaDTO implements Serializable {
    private String nombre;
    public CategoriaDTO(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser vacio");
        this.nombre = nombre;
    }
}

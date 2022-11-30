package taller2.DTOs;

import main.java.taller1.Logica.Clases.E_EstadoEspectaculo;

public class EspectaculoNuevoEstadoDTO {
    private String nombrePlataforma;
    private String nombreEspectaculo;
    private E_EstadoEspectaculo nuevoEstado;

    public EspectaculoNuevoEstadoDTO(){}

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public void setNombrePlataforma(String nombrePlataforma) {
        if (nombrePlataforma == null || nombrePlataforma.isEmpty())
            throw new IllegalArgumentException("El nombre de la plataforma no puede estar vacio");
        this.nombrePlataforma = nombrePlataforma;
    }

    public String getNombreEspectaculo() {
        return nombreEspectaculo;
    }

    public void setNombreEspectaculo(String nombreEspectaculo) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty())
            throw new IllegalArgumentException("El nombre del espectaculo no puede estar vacio");
        this.nombreEspectaculo = nombreEspectaculo;
    }

    public E_EstadoEspectaculo getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(E_EstadoEspectaculo nuevoEstado) {
        if (nuevoEstado == null)
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        this.nuevoEstado = nuevoEstado;
    }
}

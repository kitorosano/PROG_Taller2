package taller2.DTOs;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PaqueteDTO implements Serializable {
    private String nombre;
    private String descripcion;
    private double descuento;
    private String fechaExpiracion;
    private String fechaRegistro;
    private String imagen;

    public PaqueteDTO(){}

    public PaqueteDTO(String nombre, String descripcion, double descuento, String fechaExpiracion, String fechaRegistro, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaRegistro = fechaRegistro;
        this.imagen = imagen;
    }

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

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

package taller2.DTOs;

import java.time.LocalDateTime;

public class PaqueteDTO {
    private String nombre;
    private String descripcion;
    private double descuento;
    private LocalDateTime fechaExpiracion;
    private LocalDateTime fechaRegistro;
    private String imagen;

    public PaqueteDTO(){}

    public PaqueteDTO(String nombre, String descripcion, double descuento, LocalDateTime fechaExpiracion, LocalDateTime fechaRegistro, String imagen) {
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
        if (nombre == null || nombre.isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser vacio");
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty())
            throw new IllegalArgumentException("La descripcion no puede ser vacia");
        this.descripcion = descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        if (fechaExpiracion == null)
            throw new IllegalArgumentException("Fecha de Expiracion no puede ser vacia");
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        if (fechaRegistro == null)
            throw new IllegalArgumentException("Fecha de registro no puede ser vacia");
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        if (imagen == null || imagen.isEmpty())
            throw new IllegalArgumentException("La imagen no puede ser vacio");
        this.imagen = imagen;
    }
}

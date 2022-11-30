package taller2.DTOs;


import java.time.LocalDateTime;

public class FuncionDTO {
    private String nombre;
    private EspectaculoDTO espectaculo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaRegistro;
    String imagen;

    public FuncionDTO(){}

    public EspectaculoDTO getEspectaculo() {
        return espectaculo;
    }

    public void setEspectaculo(EspectaculoDTO espectaculo) {
        this.espectaculo = espectaculo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty())
            throw new IllegalArgumentException("El nombre no puede ser vacio");
        this.nombre = nombre;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        if (fechaHoraInicio == null)
            throw new IllegalArgumentException("Fecha y hora de inicio no puede ser vacia");
        this.fechaHoraInicio = fechaHoraInicio;
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

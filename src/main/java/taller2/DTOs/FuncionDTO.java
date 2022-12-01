package taller2.DTOs;


import java.io.Serializable;
import java.time.LocalDateTime;

public class FuncionDTO implements Serializable {
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
        this.nombre = nombre;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

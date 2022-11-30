package taller2.DTOs;

import java.time.LocalDateTime;

public class EspectadorPaqueteDTO {
  private UsuarioDTO espectador;
  private PaqueteDTO paquete;
  private LocalDateTime fechaRegistro;
  
  public EspectadorPaqueteDTO(){}
  
  
  public UsuarioDTO getEspectador() {
    return espectador;
  }
  public void setEspectador(UsuarioDTO espectador) {
    this.espectador = espectador;
  }
  
  public PaqueteDTO getPaquete() {
    return paquete;
  }
  public void setPaquete(PaqueteDTO paquete) {
    this.paquete = paquete;
  }
  
  public LocalDateTime getFechaRegistro() {
    return fechaRegistro;
  }
  public void setFechaRegistro(LocalDateTime fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }
  
}

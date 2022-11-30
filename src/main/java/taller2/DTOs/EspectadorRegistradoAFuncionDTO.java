package taller2.DTOs;

import java.time.LocalDateTime;

public class EspectadorRegistradoAFuncionDTO {
  
  private UsuarioDTO espectador;
  private FuncionDTO funcion;
  private PaqueteDTO paquete;
  private boolean canjeado;
  private double costo;
  private LocalDateTime fechaRegistro;
  
  public EspectadorRegistradoAFuncionDTO() {
  }
  
  public UsuarioDTO getEspectador() {
    return espectador;
  }
  public void setEspectador(UsuarioDTO espectador) {
    this.espectador = espectador;
  }
  
  public FuncionDTO getFuncion() {
    return funcion;
  }
  public void setFuncion(FuncionDTO funcion) {
    this.funcion = funcion;
  }
  
  public PaqueteDTO getPaquete() {
    return paquete;
  }
  public void setPaquete(PaqueteDTO paquete) {
    this.paquete = paquete;
  }
  
  public boolean isCanjeado() {
    return canjeado;
  }
  public void setCanjeado(boolean canjeado) {
    this.canjeado = canjeado;
  }
  
  public double getCosto() {
    return costo;
  }
  public void setCosto(double costo) {
    this.costo = costo;
  }
  
  public LocalDateTime getFechaRegistro() {
    return fechaRegistro;
  }
  public void setFechaRegistro(LocalDateTime fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }
}

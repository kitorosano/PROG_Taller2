package taller2.DTOs;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EspectadorRegistradoAFuncionDTO implements Serializable {
  
  private String espectador;
  private FuncionDTO funcion;
  private PaqueteDTO paquete;
  private boolean canjeado;
  private double costo;
  private String fechaRegistro;
  
  public EspectadorRegistradoAFuncionDTO() {
  }
  
  public String getEspectador() {
    return espectador;
  }
  public void setEspectador(String espectador) {
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
  
  public String getFechaRegistro() {
    return fechaRegistro;
  }
  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }
}

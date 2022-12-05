package taller2.DTOs;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AltaEspectadorRegistradoAFuncionDTO implements Serializable {
  
  private String espectador;
  private String funcion;
  private String espectaculo;
  private String plataforma;
  private String paquete;
  private boolean canjeado;
  private double costo;
  private String fechaRegistro;
  
  public AltaEspectadorRegistradoAFuncionDTO() {
  }
  
  public String getEspectador() {
    return espectador;
  }
  public void setEspectador(String espectador) {
    this.espectador = espectador;
  }
  
  public String getFuncion() {
    return funcion;
  }
  public void setFuncion(String funcion) {
    this.funcion = funcion;
  }
  
  public String getEspectaculo() {
    return espectaculo;
  }
  public void setEspectaculo(String espectaculo) {
    this.espectaculo = espectaculo;
  }
  
  public String getPlataforma() {
    return plataforma;
  }
  public void setPlataforma(String plataforma) {
    this.plataforma = plataforma;
  }
  
  public String getPaquete() {
    return paquete;
  }
  public void setPaquete(String paquete) {
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

package taller2.DTOs;

import java.io.Serializable;

public class AltaEspectaculoAPaqueteDTO implements Serializable {
  public String nombrePaquete;
  public String nombreEspectaculo;
  public String nombrePlataforma;
  
  public AltaEspectaculoAPaqueteDTO() {
  }
  
  
  public String getNombrePaquete() {
    return nombrePaquete;
  }
  
  public void setNombrePaquete(String nombrePaquete) {
    this.nombrePaquete = nombrePaquete;
  }
  
  public String getNombreEspectaculo() {
    return nombreEspectaculo;
  }
  
  public void setNombreEspectaculo(String nombreEspectaculo) {
    this.nombreEspectaculo = nombreEspectaculo;
  }
  
  public String getNombrePlataforma() {
    return nombrePlataforma;
  }
  
  public void setNombrePlataforma(String nombrePlataforma) {
    this.nombrePlataforma = nombrePlataforma;
  }
  
}

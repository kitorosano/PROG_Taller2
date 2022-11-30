package taller2.DTOs;

public class AltaEspectadorAPaqueteDTO {
  String nickname;
  String nombrePaquete;
  String fechaRegistro;
  
  public AltaEspectadorAPaqueteDTO() {
  }
  
  public String getNickname() {
    return nickname;
  }
  
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
  
  public String getNombrePaquete() {
    return nombrePaquete;
  }
  
  public void setNombrePaquete(String nombrePaquete) {
    this.nombrePaquete = nombrePaquete;
  }
  
  public String getFechaRegistro() {
    return fechaRegistro;
  }
  
  public void setFechaRegistro(String fechaRegistro) {
    this.fechaRegistro = fechaRegistro;
  }
}

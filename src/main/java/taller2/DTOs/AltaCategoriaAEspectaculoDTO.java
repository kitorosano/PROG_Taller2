package taller2.DTOs;

public class AltaCategoriaAEspectaculoDTO {
  
  private String nombreCategoria;
  private String nombreEspectaculo;
  private String nombrePlataforma;
  
  public AltaCategoriaAEspectaculoDTO() {
  }
  
  public String getNombreCategoria() {
    return nombreCategoria;
  }
  public void setNombreCategoria(String nombreCategoria) {
    this.nombreCategoria = nombreCategoria;
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

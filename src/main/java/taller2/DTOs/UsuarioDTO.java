package taller2.DTOs;

import java.io.Serializable;
import java.time.LocalDate;

public class UsuarioDTO implements Serializable {
  
  private String nickname;
  private String nombre;
  private String apellido;
  private String correo;
  private String fechaNacimiento;
  private String contrasenia;
  private String imagen;
  private String descripcion;
  private String biografia;
  private String sitioWeb;
  private boolean esArtista = false;
  
  public UsuarioDTO() {
  }
  
  public String getNickname() {
    return nickname;
  }
  
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
  
  public String getNombre() {
    return nombre;
  }
  
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  
  public String getApellido() {
    return apellido;
  }
  
  public void setApellido(String apellido) {
    this.apellido = apellido;
  }
  
  public String getCorreo() {
    return correo;
  }
  
  public void setCorreo(String correo) {
    this.correo = correo;
  }
  
  public String getFechaNacimiento() {
    return fechaNacimiento;
  }
  
  public void setFechaNacimiento(String fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }
  
  public String getContrasenia() {
    return contrasenia;
  }
  
  public void setContrasenia(String contrasenia) {
    this.contrasenia = contrasenia;
  }
  
  public String getImagen() {
    return imagen;
  }
  
  public void setImagen(String imagen) {
    this.imagen = imagen;
  }
  
  
  public String getDescripcion() {
    return descripcion;
  }
  
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
  
  public String getBiografia() {
    return biografia;
  }
  
  public void setBiografia(String biografia) {
    this.biografia = biografia;
  }
  
  public String getSitioWeb() {
    return sitioWeb;
  }
  
  public void setSitioWeb(String sitioWeb) {
    this.sitioWeb = sitioWeb;
  }
  
  public boolean isEsArtista() {
    return esArtista;
  }
  
  public void setEsArtista(boolean esArtista) {
    this.esArtista = esArtista;
  }
}

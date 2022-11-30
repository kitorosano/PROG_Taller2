package taller2.DTOs;


import taller2.E_EstadoEspectaculo;

import java.time.LocalDateTime;

public class AltaEspectaculoDTO {
  
  private String nombre;
  private String descripcion;
  private double duracion;
  private int minEspectadores;
  private int maxEspectadores;
  private String url;
  private double costo;
  private E_EstadoEspectaculo estado;
  private LocalDateTime fechaRegistro;
  private String imagen;
  private String plataforma;
  private String artista;
  
  public AltaEspectaculoDTO() {
  }
  
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  
  public String getDescripcion() {
    return descripcion;
  }
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
  
  public double getDuracion() {
    return duracion;
  }
  public void setDuracion(double duracion) {
    this.duracion = duracion;
  }
  
  public int getMinEspectadores() {
    return minEspectadores;
  }
  public void setMinEspectadores(int minEspectadores) {
    this.minEspectadores = minEspectadores;
  }
  
  public int getMaxEspectadores() {
    return maxEspectadores;
  }
  public void setMaxEspectadores(int maxEspectadores) {
    this.maxEspectadores = maxEspectadores;
  }
  
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  
  public double getCosto() {
    return costo;
  }
  public void setCosto(double costo) {
    this.costo = costo;
  }
  
  public E_EstadoEspectaculo getEstado() {
    return estado;
  }
  public void setEstado(E_EstadoEspectaculo estado) {
    this.estado = estado;
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
  
  public String getPlataforma() {
    return plataforma;
  }
  public void setPlataforma(String nombrePlataforma) {
    this.plataforma = nombrePlataforma;
  }
  
  public String getArtista() {
    return artista;
  }
  public void setArtista(String artista) {
    this.artista = artista;
  }
  
}

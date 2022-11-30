package taller2.DTOs;

import main.java.taller1.Logica.Clases.E_EstadoEspectaculo;

import java.time.LocalDateTime;

public class EspectaculoDTO {
  
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
  private PlataformaDTO plataforma;
  private UsuarioDTO artista;
  
  private boolean esFavorito;
  private int cantidadFavoritos;
  
  public EspectaculoDTO() {
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
  
  public PlataformaDTO getPlataforma() {
    return plataforma;
  }
  public void setPlataforma(PlataformaDTO nombrePlataforma) {
    this.plataforma = nombrePlataforma;
  }
  
  public UsuarioDTO getArtista() {
    return artista;
  }
  public void setArtista(UsuarioDTO artista) {
    this.artista = artista;
  }
  
  public boolean isEsFavorito() {
    return esFavorito;
  }
  public void setEsFavorito(boolean esFavorito) {
    this.esFavorito = esFavorito;
  }
  
  public int getCantidadFavoritos() {
    return cantidadFavoritos;
  }
  public void setCantidadFavoritos(int cantidadFavoritos) {
    this.cantidadFavoritos = cantidadFavoritos;
  }
  
}

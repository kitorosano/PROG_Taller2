package taller2.DTOs;

import java.io.Serializable;

public class EspectaculoFavoritoDTO implements Serializable {
    private String nombrePlataforma;
    private String nombreEspectaculo;
    private String nickname;

    public EspectaculoFavoritoDTO(){}

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public void setNombrePlataforma(String nombrePlataforma) {
        this.nombrePlataforma = nombrePlataforma;
    }

    public String getNombreEspectaculo() {
        return nombreEspectaculo;
    }

    public void setNombreEspectaculo(String nombreEspectaculo) {
        this.nombreEspectaculo = nombreEspectaculo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

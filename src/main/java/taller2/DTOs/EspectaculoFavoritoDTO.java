package taller2.DTOs;

public class EspectaculoFavoritoDTO {
    private String nombrePlataforma;
    private String nombreEspectaculo;
    private String nickname;

    public EspectaculoFavoritoDTO(){}

    public String getNombrePlataforma() {
        return nombrePlataforma;
    }

    public void setNombrePlataforma(String nombrePlataforma) {
        if (nombrePlataforma == null || nombrePlataforma.isEmpty())
            throw new IllegalArgumentException("El nombre de la plataforma no puede ser vacio");
        this.nombrePlataforma = nombrePlataforma;
    }

    public String getNombreEspectaculo() {
        return nombreEspectaculo;
    }

    public void setNombreEspectaculo(String nombreEspectaculo) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty())
            throw new IllegalArgumentException("El nombre del espectaculo no puede ser vacio");
        this.nombreEspectaculo = nombreEspectaculo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isEmpty())
            throw new IllegalArgumentException("El nickname no puede ser vacio");
        this.nickname = nickname;
    }
}

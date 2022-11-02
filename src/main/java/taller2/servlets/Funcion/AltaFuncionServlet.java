package taller2.servlets.Funcion;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Funcion;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AltaFuncion", value = "/registro-funcion")
@MultipartConfig
public class AltaFuncionServlet extends HttpServlet {

    Fabrica fabrica;

    public void init() {
        fabrica = Fabrica.getInstance();
    }

    protected void dispatchPage(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher view = request.getRequestDispatcher(page);
        view.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String artista="Domainer2";
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(artista);
        /* for(Espectaculo esp:espectaculos.values()){
            if(esp.getEstado()!=E_EstadoEspectaculo.ACEPTADO){
                espectaculos.remove(esp.getNombre()+"-"+esp.getPlataforma().getNombre(),esp);
            }
        }   COMENTADO HASTA IMPLEMENTAR EL CAMBIO DE ESTADO*/

        List<String> artistas=obtenerArtistas(artista);
        request.setAttribute("espectaculos", espectaculos);
        request.setAttribute("artistas",artistas);
        dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombrespectaculo = request.getParameter("espectaculo");
        String nombrefuncion= request.getParameter("nombre");
        String fecha = request.getParameter("fechaInicio");
        String hora = request.getParameter("horaInicio");
        String urlImagen="";
        Part part=request.getPart("imagen");
        String artista="Domainer2";
        String[] artistasInvitados = request.getParameterValues("artInvitado");
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(artista);
        List<String> artistas=obtenerArtistas(artista);
        if(artistasInvitados!=null){
            for(String invitado:artistasInvitados){
                artistas.remove(invitado);
            }
        }
        request.setAttribute("artistas",artistas);
       /* for(Espectaculo esp:espectaculos.values()){
            if(esp.getEstado()!=E_EstadoEspectaculo.ACEPTADO){
                espectaculos.remove(esp.getNombre()+"-"+esp.getPlataforma().getNombre(),esp);
            }
        }   COMENTADO HASTA IMPLEMENTAR EL CAMBIO DE ESTADO*/
        request.setAttribute("espectaculos", espectaculos);

        if(camposVacios(nombrefuncion,nombrespectaculo,fecha,hora,artista)){
            request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }
        Espectaculo esp=espectaculos.get(nombrespectaculo);
        if(nombreExistente(nombrefuncion,esp)){
            request.setAttribute("error", "El nombre ingresado ya existe");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }
        if(fechaInvalida(fecha)){
            request.setAttribute("error", "Fecha invalida");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }
        if(horaInvalida(hora)){
            request.setAttribute("error", "Hora invalida");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }
        LocalDateTime fechahora= LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora));
        if(part.getSize()!=0){
            InputStream inputImagen=part.getInputStream();
            urlImagen=fabrica.getIDatabase().guardarImagen((FileInputStream) inputImagen);
        }
        Funcion nueva=new Funcion(nombrefuncion,esp,fechahora,LocalDateTime.now(), urlImagen);
        try {
            fabrica.getIFuncion().altaFuncion(nueva);
            //TODO: VER COMO AGREGAR ARTISTAS INVITADOS
            response.sendRedirect(request.getContextPath()); // redirigir a un servlet (por url)
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            request.setAttribute("error", "Error al crear la funcion");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }

    private boolean camposVacios(String nombre, String nombespectaculo, String fecha, String hora, String nombArtista) {
        return nombre == null || nombespectaculo == null || fecha == null || nombArtista == null || hora == null ||
                nombre.isEmpty() || nombespectaculo.isEmpty() || fecha.isEmpty() || hora.isEmpty() || nombArtista.isEmpty();
    }

    private boolean nombreExistente(String nombrefunc, Espectaculo esp) {      //Devuelve true si hay error
        Map<String, Funcion> funciones = fabrica.getIFuncion().obtenerFuncionesDeEspectaculo(esp.getPlataforma().getNombre(),esp.getNombre());
        for (Funcion fun : funciones.values()) {
            if (fun.getNombre().equals(nombrefunc)) {
                return true;
            }
        }
        return false;
    }
    private boolean fechaInvalida(String fecha){
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
            return false;
        } catch (ParseException e) {
            System.out.println("ERROR EN LA FECHA");
            return true;
        }
    }

    private boolean horaInvalida(String hora){
        try {
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
            formatoHora.setLenient(false);
            formatoHora.parse(hora);
            return false;
        } catch (ParseException e) {
            System.out.println("ERROR EN LA HORA");
            return true;
        }
    }

    private List<String> obtenerArtistas(String artista){
        Map <String, Usuario> usuarios = Fabrica.getInstance().getIUsuario().obtenerUsuarios();
        List<String> artistas = new ArrayList<>();
        for(Usuario u:usuarios.values()){
            if(u instanceof Artista && !artista.equals(u.getNickname())){
                artistas.add(u.getNickname());
            }
        }
        return artistas;
    }

}

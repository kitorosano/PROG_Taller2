package taller2.servlets.Funcion;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import main.java.taller1.Logica.Clases.*;
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
import java.util.HashMap;
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
        boolean esArtista= (boolean) request.getSession().getAttribute("esArtista");
        if(esArtista){
            Artista art=(Artista) request.getSession().getAttribute("usuarioLogueado");
            String artista=art.getNickname();
            Map<String, Espectaculo> retorno = new HashMap<>();
            Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(artista);
            System.out.println(espectaculos);
            for(Espectaculo esp:espectaculos.values()){
                if(esp.getEstado()==E_EstadoEspectaculo.ACEPTADO){
                    retorno.put(esp.getNombre()+"-"+esp.getPlataforma().getNombre(),esp);
                }
            }

            List<String> artistas=obtenerArtistas(artista);
            request.setAttribute("espectaculos", retorno);
            request.setAttribute("artistas",artistas);
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }else{
            System.out.println("No puede acceder a esta pagina");
            request.setAttribute("error", "No puede acceder a esta pagina");
            dispatchPage("/pages/index.jsp", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombrespectaculo = request.getParameter("espectaculo");
        String nombrefuncion= request.getParameter("nombre");
        String fecha = request.getParameter("fechaInicio");
        String hora = request.getParameter("horaInicio");
        String urlImagen="https://i.imgur.com/EDotlnM.png";
        Part part=request.getPart("imagen");
        String[] artistasInvitados = request.getParameterValues("artInvitado");
        Artista art=(Artista) request.getSession().getAttribute("usuarioLogueado");
        String artista=art.getNickname();
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(artista);
        Map<String, Espectaculo> retorno = new HashMap<>();
        List<String> artistas=obtenerArtistas(artista);
        if(artistasInvitados!=null){
            for(String invitado:artistasInvitados){
                artistas.remove(invitado);
            }
        }
        request.setAttribute("artistas",artistas);
       for(Espectaculo esp:espectaculos.values()){
            if(esp.getEstado()== E_EstadoEspectaculo.ACEPTADO){
                retorno.put(esp.getNombre()+"-"+esp.getPlataforma().getNombre(),esp);
            }
        }
        request.setAttribute("espectaculos", retorno);

        if(camposVacios(nombrefuncion,nombrespectaculo,fecha,hora,artista)){
            request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }
        Espectaculo esp=retorno.get(nombrespectaculo);
        if(esp==null){
            request.setAttribute("error", "El espectaculo está en estado ingresado");
            dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
        }
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
        if(funciones!=null) {
            for (Funcion fun : funciones.values()) {
                if (fun.getNombre().equals(nombrefunc)) {
                    return true;
                }
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

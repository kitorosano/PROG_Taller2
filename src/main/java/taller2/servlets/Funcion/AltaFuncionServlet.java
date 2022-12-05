package taller2.servlets.Funcion;


import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import taller2.DTOs.*;
import taller2.E_EstadoEspectaculo;
import taller2.utils.Fetch;

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
    
    Fetch fetch;
    
    public void init() {
        fetch = new Fetch();
    }

    protected void dispatchPage(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher view = request.getRequestDispatcher(page);
        view.forward(request, response);
    }
    
    protected boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Si no hay sesión, redirigir a login
        if (session == null) {
            return false;
        }
        
        // Si hay sesión, obtener el usuario
        UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        
        // Si no hay usuario, redirigir a login
        if (usuarioLogueado == null) {
            return false;
        }
        
        // Si hay usuario, enviarlo a la página de inicio
        return true;
    }
    
    protected void dispatchError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("message", errorMessage);
        request.setAttribute("messageType","error");
        RequestDispatcher view = request.getRequestDispatcher("/pages/funcion/registro-funcion.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si no hay sesión, redirigir a login
        boolean sessionIniciada = checkSession(request, response);
        try {
            if(sessionIniciada) {
                
                Map<String, PlataformaDTO> todasPlataformas = fetch.Set("/plataformas/findAll").Get().getMapPlataforma();
                Map<String, EspectaculoDTO> todosEspectaculos = fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
                Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll").Get().getMapPaquete();
                Map<String, CategoriaDTO> todasCategorias = fetch.Set("/categorias/findAll").Get().getMapCategoria();
                Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll").Get().getMapUsuario();
    
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
    
                HttpSession session = request.getSession();
                boolean esArtista= (boolean) session.getAttribute("esArtista");
                if(esArtista){
                    UsuarioDTO art=(UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");
                    String artista=art.getNickname();
                    Map<String, EspectaculoDTO> retorno = new HashMap<>();
                    //Map<String, EspectaculoDTO> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(artista);
                    Map<String, EspectaculoDTO> espectaculos= fetch.Set("/espectaculos/findByArtista?artistaOrganizador="+artista).Get().getMapEspectaculo();
                    for(EspectaculoDTO esp:espectaculos.values()){
                        if(esp.getEstado()== E_EstadoEspectaculo.ACEPTADO){
                            retorno.put(esp.getNombre()+"-"+esp.getPlataforma().getNombre(),esp);
                        }
                    }
        
                    List<String> artistas=obtenerArtistas(artista);
                    request.setAttribute("espectaculos", retorno);
                    request.setAttribute("artistas",artistas);
                    dispatchPage("/pages/funcion/registro-funcion.jsp", request, response);
                }else{
                    dispatchPage("/pages/404.jsp", request, response);
                }
            } else {
                response.sendRedirect("login");
            }
        } catch (RuntimeException e) {
            dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
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
        //Artista art=(Artista) request.getSession().getAttribute("usuarioLogueado");
        UsuarioDTO art=(UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");
        String artista=art.getNickname();
        //Map<String, EspectaculoDTO> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(artista);
        Map<String, EspectaculoDTO> espectaculos=fetch.Set("/espectaculos/findByArtista?artistaOrganizador="+artista).Get().getMapEspectaculo();
        Map<String, EspectaculoDTO> retorno = new HashMap<>();
        List<String> artistas=obtenerArtistas(artista);
        if(artistasInvitados!=null){
            for(String invitado:artistasInvitados){
                artistas.remove(invitado);
            }
        }
        request.setAttribute("artistas",artistas);
       for(EspectaculoDTO esp:espectaculos.values()){
            if(esp.getEstado()== E_EstadoEspectaculo.ACEPTADO){
                retorno.put(esp.getNombre()+"-"+esp.getPlataforma().getNombre(),esp);
            }
        }
        request.setAttribute("espectaculos", retorno);

        if(camposVacios(nombrefuncion,nombrespectaculo,fecha,hora,artista)){
            dispatchError("Los campos obligatorios no pueden ser vacios", request, response);
            return;
        }
        EspectaculoDTO esp=retorno.get(nombrespectaculo);
        if(esp==null){
            dispatchError("El espectaculo está en estado ingresado", request, response);
            return;
        }
        if(nombreExistente(nombrefuncion,esp)){
            dispatchError("El nombre ingresado ya existe", request, response);
            return;
        }
        if(fechaInvalida(fecha)){
            dispatchError("Fecha invalida", request, response);
            return;
        }
        if(horaInvalida(hora)){
            dispatchError("Hora invalida", request, response);
            return;
        }
        LocalDateTime fechahora= LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora));
        if(part.getSize()!=0){
            InputStream inputImagen=part.getInputStream();
            urlImagen= fetch.Set("/database/createImage",inputImagen).Get().getString();
            //urlImagen=fabrica.getIDatabase().guardarImagen((FileInputStream) inputImagen);
        }
        FuncionDTO nueva=new FuncionDTO();
        nueva.setNombre(nombrefuncion);
        nueva.setEspectaculo(esp);
        nueva.setFechaHoraInicio(fechahora.toString());
        nueva.setFechaRegistro(LocalDateTime.now().toString());
        nueva.setImagen(urlImagen);
        try {
            fetch.Set("/funciones/create",nueva).Post();
            //fabrica.getIFuncion().altaFuncion(nueva);
            response.sendRedirect(request.getContextPath()); // redirigir a un servlet (por url)
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            dispatchError("Error al crear la funcion", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }

    private boolean camposVacios(String nombre, String nombespectaculo, String fecha, String hora, String nombArtista) {
        return nombre == null || nombespectaculo == null || fecha == null || nombArtista == null || hora == null ||
                nombre.isEmpty() || nombespectaculo.isEmpty() || fecha.isEmpty() || hora.isEmpty() || nombArtista.isEmpty();
    }

    private boolean nombreExistente(String nombrefunc, EspectaculoDTO esp) {      //Devuelve true si hay error
        //Map<String, FuncionDTO> funciones = fabrica.getIFuncion().obtenerFuncionesDeEspectaculo(esp.getPlataforma().getNombre(),esp.getNombre());
        try {
            Map<String, FuncionDTO> funciones= fetch.Set("/funciones/findByEspectaculoAndPlataforma?nombrePlataforma="+esp.getPlataforma().getNombre()+"&nombreEspectaculo="+esp.getNombre()).Get().getMapFuncion();
            if(funciones!=null) {
                for (FuncionDTO fun : funciones.values()) {
                    if (fun.getNombre().equals(nombrefunc)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            //Map <String, UsuarioDTO> usuarios = Fabrica.getInstance().getIUsuario().obtenerUsuarios();
            Map <String, UsuarioDTO> usuarios= fetch.Set("/usuarios/findAll").Get().getMapUsuario();
            List<String> artistas = new ArrayList<>();
            for(UsuarioDTO u:usuarios.values()){
                if(u.isEsArtista() && !artista.equals(u.getNickname())){
                    artistas.add(u.getNickname());
                }
            }
            return artistas;
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

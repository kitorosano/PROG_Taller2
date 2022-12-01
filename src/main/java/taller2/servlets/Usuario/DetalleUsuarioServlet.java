package taller2.servlets.Usuario;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleUsuario", value = "/perfil")
public class DetalleUsuarioServlet extends HttpServlet {
  
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
    RequestDispatcher view = request.getRequestDispatcher("/pages/index.jsp");
    view.forward(request, response);
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Si no hay sesión, redirigir a login
    boolean sessionIniciada = checkSession(request, response);
    try {
      if(sessionIniciada) {
        Map<String, PlataformaDTO> todasPlataformas = fetch.Set("/plataformas/findAll/").Get().getContentMap(PlataformaDTO.class);
        Map<String, EspectaculoDTO> todosEspectaculos = fetch.Set("/espectaculos/findAll/").Get().getContentMap(EspectaculoDTO.class);
        Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll/").Get().getContentMap(PaqueteDTO.class);
        Map<String, CategoriaDTO> todasCategorias  = fetch.Set("/categorias/findAll/").Get().getContentMap(CategoriaDTO.class);
        Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll/").Get().getContentMap(UsuarioDTO.class);
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
        
        HttpSession session = request.getSession();
        String usuarioLogueadoNickname = ((UsuarioDTO) session.getAttribute("usuarioLogueado")).getNickname();
        String nickname = request.getParameter("nickname") != null ? request.getParameter("nickname") : usuarioLogueadoNickname;
        Boolean esPerfilPropio = nickname.equals(usuarioLogueadoNickname);
        request.setAttribute("esPerfilPropio", esPerfilPropio);
      
        UsuarioDTO usuario;
        // Si el usuario no viene vacio y no es mi perfil entonces buscar por nickname
        if(!nickname.isEmpty() && !esPerfilPropio) {
          //boolean usuarioExistePorNickname = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent();
          UsuarioDTO usuarioExistePorNickname =fetch.Set("/usuarios/findByNickname?nickname="+nickname).Get().getContent(UsuarioDTO.class);
          if (usuarioExistePorNickname==null) { // Si el usuario no existe por nickname, buscar por email
            //boolean usuarioExistePorCorreo = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname).isPresent();
            UsuarioDTO usuarioExistePorCorreo = fetch.Set("/usuarios/findByCorreo?correo="+nickname).Get().getContent(UsuarioDTO.class);
            if (usuarioExistePorCorreo==null) { // Si el usuario no existe por correo, redirigir al listado de usuarios
              request.setAttribute("respuesta", "Usuario no encontrado");
              response.sendRedirect("listado-usuarios");
              return;
            }
            //usuario = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname).get();
              usuario = fetch.Set("/usuarios/findByCorreo?correo="+nickname).Get().getContent(UsuarioDTO.class);
          } else {
            //usuario = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
              usuario = fetch.Set("/usuarios/findByNickname?nickname="+nickname).Get().getContent(UsuarioDTO.class);
          }
        } else {
          // Si el usuario viene vacio o es mi perfil, traer el usuario logueado
          usuario = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        }
        request.setAttribute("datos", usuario);
        
        // Si el usuario es artista, entonces mostramos sus espectaculos
        if(usuario.isEsArtista()) {
          //Map <String, EspectaculoDTO> espectaculos=fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(usuario.getNickname());
          Map <String, EspectaculoDTO> espectaculos= fetch.Set("/espectaculos/findByArtista?artistaOrganizador="+usuario.getNickname()).Get().getContentMap(EspectaculoDTO.class);
          request.setAttribute("espectaculos", espectaculos);
        }
        // Si el usuario es espectador, entonces mostramos sus funciones a las que esta registrado y sus paquetes comprados
        else {
          //Map<String, EspectadorRegistradoAFuncionDTO> funciones=fabrica.getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuario.getNickname());
          Map<String, EspectadorRegistradoAFuncionDTO> funciones = fetch.Set("/EspectadorAFuncion/findByNickname?nicknameEspectador="+usuario.getNickname()).Get().getContentMap(EspectadorRegistradoAFuncionDTO.class);
          request.setAttribute("funciones",funciones);
          
          //Map<String, AltaEspectadorAPaqueteDTO> paquetes=fabrica.getIPaquete().obtenerPaquetesPorEspectador(usuario.getNickname());
          Map<String, PaqueteDTO> paquetes= fetch.Set("/paquetes/findByNombreEspectador?nombreEspectador="+usuario.getNickname()).Get().getContentMap(PaqueteDTO.class);
          request.setAttribute("paquetes",paquetes);
        }
        
        dispatchPage("/pages/usuario/perfil.jsp", request, response);
      } else {
        response.sendRedirect("login");
      }
    } catch (RuntimeException e) {
      dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

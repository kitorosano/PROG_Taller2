package taller2.servlets.Usuario;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import taller2.DTOs.*;
import taller2.utils.Utils;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleUsuario", value = "/perfil")
public class DetalleUsuarioServlet extends HttpServlet {

  

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
        Map<String, PlataformaDTO> todasPlataformas = (Map<String, PlataformaDTO>) Utils.FetchApi("/plataformas/findAll/").getEntity();
        Map<String, EspectaculoDTO> todosEspectaculos = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos/findAll/").getEntity();
        Map<String, PaqueteDTO> todosPaquetes = (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes/findAll/").getEntity();
        Map<String, CategoriaDTO> todasCategorias  = (Map<String, CategoriaDTO>) Utils.FetchApi("/categorias/findAll/").getEntity();
        Map<String, UsuarioDTO> todosUsuarios = (Map<String, UsuarioDTO>) Utils.FetchApi("/usuarios/findAll/").getEntity();
      
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
          UsuarioDTO usuarioExistePorNickname =(UsuarioDTO) Utils.FetchApi("/usuarios/findByNickname/?nickname="+nickname).getEntity();
          if (usuarioExistePorNickname==null) { // Si el usuario no existe por nickname, buscar por email
            //boolean usuarioExistePorCorreo = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname).isPresent();
            UsuarioDTO usuarioExistePorCorreo = (UsuarioDTO) Utils.FetchApi("/usuarios/findByCorreo/?correo="+nickname).getEntity();
            if (usuarioExistePorCorreo==null) { // Si el usuario no existe por correo, redirigir al listado de usuarios
              request.setAttribute("respuesta", "Usuario no encontrado");
              response.sendRedirect("listado-usuarios");
              return;
            }
            //usuario = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname).get();
              usuario = (UsuarioDTO) Utils.FetchApi("/usuarios/findByCorreo/?correo="+nickname).getEntity();
          } else {
            //usuario = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
              usuario = (UsuarioDTO) Utils.FetchApi("/usuarios/findByNickname/?nickname="+nickname).getEntity();
          }
        } else {
          // Si el usuario viene vacio o es mi perfil, traer el usuario logueado
          usuario = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        }
        request.setAttribute("datos", usuario);
        
        // Si el usuario es artista, entonces mostramos sus espectaculos
        if(usuario.isEsArtista()) {
          //Map <String, EspectaculoDTO> espectaculos=fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(usuario.getNickname());
          Map <String, EspectaculoDTO> espectaculos= (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos/findByArtista/?artistaOrganizador="+usuario.getNickname()).getEntity();
          request.setAttribute("espectaculos", espectaculos);
        }
        // Si el usuario es espectador, entonces mostramos sus funciones a las que esta registrado y sus paquetes comprados
        else {
          //Map<String, EspectadorRegistradoAFuncionDTO> funciones=fabrica.getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuario.getNickname());
          Map<String, EspectadorRegistradoAFuncionDTO> funciones = (Map <String, EspectadorRegistradoAFuncionDTO>) Utils.FetchApi("/EspectadorAFuncion/findByNickname/?nicknameEspectador="+usuario.getNickname()).getEntity();
          request.setAttribute("funciones",funciones);
          
          //Map<String, AltaEspectadorAPaqueteDTO> paquetes=fabrica.getIPaquete().obtenerPaquetesPorEspectador(usuario.getNickname());
          Map<String, PaqueteDTO> paquetes= (Map <String, PaqueteDTO>) Utils.FetchApi("/paquetes/findByNombreEspectador/?nombreEspectador="+usuario.getNickname()).getEntity();
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

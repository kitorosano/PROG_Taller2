package taller2.servlets.Usuario;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleUsuario", value = "/perfil")
public class DetalleUsuarioServlet extends HttpServlet {
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
    HttpSession session = request.getSession();
    String usuarioLogueadoNickname = ((Usuario) session.getAttribute("usuarioLogueado")).getNickname();
    String nickname = request.getParameter("nickname") != null ? request.getParameter("nickname") : usuarioLogueadoNickname;
    Boolean esPerfilPropio = nickname.equals(usuarioLogueadoNickname);
    request.setAttribute("esPerfilPropio", esPerfilPropio);
  
    Usuario usuario;
    // Si el usuario no viene vacio y no es mi perfil entonces buscar por nickname
    if(!nickname.isEmpty() && !esPerfilPropio) {
      boolean usuarioExistePorNickname = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent();
      if (!usuarioExistePorNickname) { // Si el usuario no existe por nickname, buscar por email
        boolean usuarioExistePorCorreo = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname).isPresent();
        if (!usuarioExistePorCorreo) { // Si el usuario no existe por correo, redirigir al listado de usuarios
          request.setAttribute("respuesta", "Usuario no encontrado");
          response.sendRedirect("listado-usuarios");
          return;
        }
        usuario = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname).get();
      } else {
        usuario = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
      }
    } else {
      // Si el usuario viene vacio o es mi perfil, traer el usuario logueado
      usuario = (Usuario) session.getAttribute("usuarioLogueado");
    }
    request.setAttribute("datos", usuario);
    
    // Si el usuario es artista, entonces mostramos sus espectaculos
    if(usuario instanceof Artista) {
      Map <String, Espectaculo> espectaculos=fabrica.getIEspectaculo().obtenerEspectaculosPorArtista(usuario.getNickname());
      request.setAttribute("espectaculos", espectaculos);
    }
    // Si el usuario es espectador, entonces mostramos sus funciones a las que esta registrado y sus paquetes comprados
    else {
      Map<String, EspectadorRegistradoAFuncion> funciones=fabrica.getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuario.getNickname());
      request.setAttribute("funciones",funciones);
      
      Map<String, EspectadorPaquete> paquetes=fabrica.getIPaquete().obtenerPaquetesPorEspectador(usuario.getNickname());
      request.setAttribute("paquetes",paquetes);
    }
    
    dispatchPage("/pages/usuario/perfil.jsp", request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

package taller2.servlets.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.CategoriaDTO;
import main.java.taller1.Logica.DTOs.EspectaculoDTO;
import main.java.taller1.Logica.DTOs.PaqueteDTO;
import main.java.taller1.Logica.DTOs.PlataformaDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ListadoUsuario", value = "/listado-usuario")
public class ListadoUsuarioServlet extends HttpServlet {
  Fabrica fabrica;
  
  public void init() {
    fabrica = Fabrica.getInstance();
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
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
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
        Map<String, PlataformaDTO> todasPlataformas = fabrica.getIPlataforma().obtenerPlataformas();
        Map<String, EspectaculoDTO> todosEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
        Map<String, PaqueteDTO> todosPaquetes = fabrica.getIPaquete().obtenerPaquetes();
        Map<String, CategoriaDTO> todasCategorias = fabrica.getICategoria().obtenerCategorias();
        Map<String, Usuario> todosUsuarios = fabrica.getIUsuario().obtenerUsuarios();
        
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
        
        dispatchPage("/pages/usuario/listado-usuario.jsp", request, response);
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

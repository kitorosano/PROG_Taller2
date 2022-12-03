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

@WebServlet(name = "ListadoUsuario", value = "/listado-usuario")
public class ListadoUsuarioServlet extends HttpServlet {
  
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
    // Si no hay sesión, redirigir a login
    boolean logueado = checkSession(request, response);
    try {
      if(!logueado) {
        response.sendRedirect("login");
        return;
      }
      Map<String, PlataformaDTO> todasPlataformas =  fetch.Set("/plataformas/findAll").Get().getMapPlataforma();
      Map<String, EspectaculoDTO> todosEspectaculos =  fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
      Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll/").Get().getMapPaquete();
      Map<String, CategoriaDTO> todasCategorias = fetch.Set("/categorias/findAll/").Get().getMapCategoria();
      Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll/").Get().getMapUsuario();
      
      request.setAttribute("todasPlataformas", todasPlataformas);
      request.setAttribute("todosEspectaculos", todosEspectaculos);
      request.setAttribute("todosPaquetes", todosPaquetes);
      request.setAttribute("todasCategorias", todasCategorias);
      request.setAttribute("todosUsuarios", todosUsuarios);
      
      dispatchPage("/pages/usuario/listado-usuario.jsp", request, response);
    } catch (RuntimeException e) {
        dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

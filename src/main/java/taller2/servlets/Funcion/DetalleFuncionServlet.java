package taller2.servlets.FuncionDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.FuncionDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleFuncion", value = "/detalle-funcion")
public class DetalleFuncionServlet extends HttpServlet {
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
        Map<String, Plataforma> todasPlataformas = fabrica.getIPlataforma().obtenerPlataformas();
        Map<String, Espectaculo> todosEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
        Map<String, Paquete> todosPaquetes = fabrica.getIPaquete().obtenerPaquetes();
        Map<String, Categoria> todasCategorias = fabrica.getICategoria().obtenerCategorias();
        Map<String, Usuario> todosUsuarios = fabrica.getIUsuario().obtenerUsuarios();
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
      
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
        String nombre = request.getParameter("nombre");
        String espectaculo = request.getParameter("espectaculo");
        String plataforma =request.getParameter("plataforma");
        
        boolean funcionExiste = Fabrica.getInstance().getIFuncion().obtenerFuncion(plataforma, espectaculo, nombre).isPresent();
        if(!funcionExiste) { // Si el espectaculo no existe
          request.setAttribute("message","Funcion no encontrada");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-funciones");
          return;
        }
        FuncionDTO funcion = Fabrica.getInstance().getIFuncion().obtenerFuncion(plataforma, espectaculo, nombre).get();
        request.setAttribute("datos",funcion);
      
        Map <String, EspectadorRegistradoAFuncion> espectadores=Fabrica.getInstance().getIFuncion().obtenerEspectadoresRegistradosAFuncion(nombre);
        request.setAttribute("espectadores",espectadores);
      
        if(esEspectador) {
          Map<String, EspectadorRegistradoAFuncion> funciones_registradas = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuarioLogueado.getNickname());
        
          if(funciones_registradas.containsKey(nombre)) {
            request.setAttribute("message","Registrado a funcion");
            request.setAttribute("datosRegistro", funciones_registradas.get(nombre));
          }
        }
        
        dispatchPage("/pages/funcion/detalle-funcion.jsp" , request, response);
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

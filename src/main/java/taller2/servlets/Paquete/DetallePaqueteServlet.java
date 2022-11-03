package taller2.servlets.Paquete;

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

@WebServlet(name = "DetallePaquete", value = "/detalle-paquete")
public class DetallePaqueteServlet extends HttpServlet {
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
        Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        String nombre = request.getParameter("nombre");
    
        boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    
        if(!paqueteExiste) { // Si el paquete no existe
          request.setAttribute("message","Paquete no encontrado");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-paquetes");
          return;
        }
        Paquete paquete = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).get();
        request.setAttribute("datos",paquete);
        
        Map<String, Espectaculo> espectaculos = Fabrica.getInstance().getIPaquete().obtenerEspectaculosDePaquete(nombre);
        request.setAttribute("espectaculos",espectaculos);
        
        if(esEspectador) {
            Map<String, EspectadorPaquete> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(usuarioLogueado.getNickname());
            
            if(paquetes_espectador.containsKey(nombre)) {
                request.setAttribute("message","Paquete Adquirido");
                request.setAttribute("fechaCompra", paquetes_espectador.get(nombre).getFechaRegistro());
            }
        }
        
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
      } else {
        response.sendRedirect("login");
      }
    } catch (RuntimeException e) {
      dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String nombre = request.getParameter("nombre");
    //String nickname_espectador = (String) session.getAttribute("nickname");
    Usuario usuSession=(Usuario)session.getAttribute("usuarioLogueado");
    String nickname_espectador= usuSession.getNickname();
    boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    if(!paqueteExiste) { // Si el paquete no existe
        request.setAttribute("message","Paquete no encontrado");
        request.setAttribute("messageType","error");
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
        return;
    }
  
    Map<String, EspectadorPaquete> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname_espectador);
    boolean paqueteYaComprado = paquetes_espectador.containsKey(nombre); // Si el paquete no está comprado, paquete_comprado es null

    if (paqueteYaComprado) {
      request.setAttribute("message", "Paquete ya está comprado");
      request.setAttribute("messageType","error");
      dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
      return;
    }
    
    Fabrica.getInstance().getIPaquete().altaEspectadorAPaquete(nombre, nickname_espectador);
    request.setAttribute("message", "Paquete Adquirido");
    response.sendRedirect("detalle-paquete" + "?nombre=" + nombre);
  }
}

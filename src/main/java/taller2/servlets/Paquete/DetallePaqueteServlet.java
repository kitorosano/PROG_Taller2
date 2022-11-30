package taller2.servlets.Paquete;

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

@WebServlet(name = "DetallePaquete", value = "/detalle-paquete")
public class DetallePaqueteServlet extends HttpServlet {


  
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
        Map<String, PlataformaDTO> todasPlataformas = (Map<String, PlataformaDTO>) Utils.FetchApi("/plataformas").getEntity();
        Map<String, EspectaculoDTO> todosEspectaculos = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos").getEntity();
        Map<String, PaqueteDTO> todosPaquetes = (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes").getEntity();
        Map<String, CategoriaDTO> todasCategorias  = (Map<String, CategoriaDTO>) Utils.FetchApi("/categorias").getEntity();
        Map<String, UsuarioDTO> todosUsuarios = (Map<String, UsuarioDTO>) Utils.FetchApi("/usuarios").getEntity();
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
      
        HttpSession session = request.getSession();
        Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
        UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        String nombre = request.getParameter("nombre");
    
        boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    
        if(!paqueteExiste) { // Si el paquete no existe
          request.setAttribute("message","Paquete no encontrado");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-paquetes");
          return;
        }
        PaqueteDTO paquete = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).get();
        request.setAttribute("datos",paquete);
        
        Map<String, EspectaculoDTO> espectaculos = Fabrica.getInstance().getIPaquete().obtenerEspectaculosDePaquete(nombre);
        request.setAttribute("espectaculos",espectaculos);
        
        if(esEspectador) {
            Map<String, AltaEspectadorAPaqueteDTO> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(usuarioLogueado.getNickname());
            
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
    UsuarioDTO usuSession=(UsuarioDTO)session.getAttribute("usuarioLogueado");
    String nickname_espectador= usuSession.getNickname();
    boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    if(!paqueteExiste) { // Si el paquete no existe
        request.setAttribute("message","Paquete no encontrado");
        request.setAttribute("messageType","error");
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
        return;
    }
  
    Map<String, EspectadorPaqueteDTO> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname_espectador);
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

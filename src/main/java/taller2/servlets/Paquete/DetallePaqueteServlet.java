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
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    String nombre = request.getParameter("nombre");
    System.out.println(nombre);
    boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    System.out.println(paqueteExiste);
    if(!paqueteExiste) { // Si el paquete no existe
      request.setAttribute("respuesta","Paquete no encontrado");
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
            request.setAttribute("respuesta","Paquete Adquirido");
            request.setAttribute("fechaCompra", paquetes_espectador.get(nombre).getFechaRegistro());
        }
    }
    
    dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String nombre = request.getParameter("nombre");
    String nickname_espectador = (String) session.getAttribute("nickname");
    
    boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    if(!paqueteExiste) { // Si el paquete no existe
        request.setAttribute("respuesta","Paquete no encontrado");
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
        return;
    }
  
    Map<String, EspectadorPaquete> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname_espectador);
    boolean paqueteYaComprado = paquetes_espectador.containsKey(nombre); // Si el paquete no está comprado, paquete_comprado es null

    if (paqueteYaComprado) {
      request.setAttribute("respuesta", "Paquete ya está comprado");
      dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
      return;
    }
    
    Fabrica.getInstance().getIPaquete().altaEspectadorAPaquete(nickname_espectador, nombre);
    request.setAttribute("respuesta", "Paquete Adquirido");
    response.sendRedirect("detalle-paquete.jsp" + "?nombre=" + nombre);
  }
}

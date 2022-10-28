package taller2.Paquete;

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
import java.util.Optional;

@WebServlet(name = "DetallePaquete", value = "/detalle-paquete")
public class DetallePaquete extends HttpServlet {
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
    response.setContentType("text/html");
    HttpSession session = request.getSession();
    Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
    String nickname = (String) session.getAttribute("nickname");
    String nombre_paquete = request.getParameter("nombre_paquete");
  
    boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre_paquete).isPresent();
    if(!paqueteExiste) { // Si el paquete no existe
      request.setAttribute("respuesta","Paquete no encontrado");
      response.sendRedirect("listado-paquetes");
      return;
    }
    
    if(esEspectador) {
        Map<String, EspectadorPaquete> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname);
        
        if(paquetes_espectador.containsKey(nombre_paquete)) {
            request.setAttribute("respuesta","Paquete Adquirido");
            request.setAttribute("datos", paquetes_espectador.get(nombre_paquete));
        }
    }
    Map<String, Espectaculo> espectaculos = Fabrica.getInstance().getIPaquete().obtenerEspectaculosDePaquete(nombre_paquete);
    request.setAttribute("espectaculos",espectaculos);

    RequestDispatcher view = request.getRequestDispatcher("/pages/paquete/detalle-paquete.jsp");
    view.forward(request, response);
    }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    HttpSession session = request.getSession();
    String nombre_paquete = request.getParameter("nombre_paquete");
    String nickname_espectador = (String) session.getAttribute("nickname");
    
    boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre_paquete).isPresent();
    if(!paqueteExiste) { // Si el paquete no existe
        request.setAttribute("respuesta","Paquete no encontrado");
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
        return;
    }
  
    Map<String, EspectadorPaquete> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname_espectador);
    boolean paqueteYaComprado = paquetes_espectador.containsKey(nombre_paquete); // Si el paquete no está comprado, paquete_comprado es null

    if (paqueteYaComprado) {
      request.setAttribute("respuesta", "Paquete ya está comprado");
      dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
      return;
    }
    
    Fabrica.getInstance().getIPaquete().altaEspectadorAPaquete(nickname_espectador, nombre_paquete);
    request.setAttribute("respuesta", "Paquete Adquirido");
    response.sendRedirect("detalle-paquete.jsp" + "?nombre_paquete=" + nombre_paquete);
  }
}

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

@WebServlet(name = "DetallePaquete", value = "/detalle-paquete")
public class DetallePaquete extends HttpServlet {
  Fabrica fabrica;

  public void init() {
    fabrica = Fabrica.getInstance();
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    HttpSession session = request.getSession();
    String nombre_paquete = request.getParameter("nombre_paquete");


    Map<String, Paquete> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetes();
    Paquete paquete = paquetes.get(nombre_paquete);
    request.setAttribute("datos",paquete);
    if((Boolean) session.getAttribute("esEspectador")) {
        Map<String, EspectadorPaquete> paquetes_comprados = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador((String) session.getAttribute("nickname"));
        Paquete paquete_comprado = paquetes_comprados.get(nombre_paquete).getPaquete();
        if(paquete_comprado != null){
            request.setAttribute("respuesta","Paquete Adquirido");
        }
    }
    Map<String, Espectaculo> espectaculos = Fabrica.getInstance().getIPaquete().obtenerEspectaculosDePaquete(paquete.getNombre());
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


    Map<String, Paquete> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetes();
    Paquete paquete = paquetes.get(nombre_paquete);// paquete para el detalle

    Map<String, EspectadorPaquete> paquetes_comprados=Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname_espectador);
    Paquete paquete_comprado = paquetes_comprados.get(nombre_paquete).getPaquete();

    if(paquete_comprado==null){
      request.setAttribute("respuesta","Paquete comprado!");

      Fabrica.getInstance().getIPaquete().altaEspectadorAPaquete(nickname_espectador,nombre_paquete);
    }
    else{
      request.setAttribute("respuesta","Paquete Adquirido");
    }

    request.setAttribute("datos",paquete);

    Map<String, Espectaculo> espectaculos = Fabrica.getInstance().getIPaquete().obtenerEspectaculosDePaquete(paquete.getNombre());
    request.setAttribute("espectaculos",espectaculos);

    RequestDispatcher view = request.getRequestDispatcher("/pages/paquete/detalle-paquete.jsp");
    view.forward(request, response);

  }
}

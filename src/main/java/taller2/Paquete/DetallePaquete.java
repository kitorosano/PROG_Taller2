package taller2.Paquete;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion;
import main.java.taller1.Logica.Clases.Funcion;
import main.java.taller1.Logica.Clases.Paquete;
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
    String nombre_paquete = request.getParameter("nombre_paquete");


    Map<String, Paquete> paquetes=Fabrica.getInstance().getIEspectaculo().obtenerPaquetes();
    Paquete paquete = paquetes.get(nombre_paquete);
    request.setAttribute("datos",paquete);

    Map<String, Espectaculo> espectaculos = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculosDePaquete(paquete.getNombre());
    request.setAttribute("espectaculos",espectaculos);

    RequestDispatcher view = request.getRequestDispatcher("/pages/paquete/detalle-paquete.jsp");
    view.forward(request, response);

    }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

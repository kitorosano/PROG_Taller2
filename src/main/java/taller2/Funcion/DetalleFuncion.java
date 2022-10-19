package taller2.Funcion;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Espectador;
import main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion;
import main.java.taller1.Logica.Clases.Funcion;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleFuncion", value = "/detalle-funcion")
public class DetalleFuncion extends HttpServlet {
  Fabrica fabrica;
  
  public void init() {
    fabrica = Fabrica.getInstance();
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    String nombre_funcion = request.getParameter("nombre_funcion");
    String nombre_espectaculo= request.getParameter("nombre_espectaculo");
    String nombre_plataforma=request.getParameter("nombre_plataforma");

    Map<String, Funcion> funciones=Fabrica.getInstance().getIEspectaculo().obtenerFuncionesDeEspectaculo(nombre_plataforma,nombre_espectaculo);
    Funcion funcion=funciones.get(nombre_funcion);


      Map <String, EspectadorRegistradoAFuncion> espectadores=Fabrica.getInstance().getIUsuario().obtenerEspectadoresRegistradosAFuncion(nombre_funcion);
       request.setAttribute("espectadores",espectadores);
       request.setAttribute("datos",funcion);
      RequestDispatcher view = request.getRequestDispatcher("/pages/funcion/detalle-funcion.jsp");
      view.forward(request, response);

    }

  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

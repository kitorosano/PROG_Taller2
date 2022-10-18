package taller2.Funcion;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.taller1.Logica.Clases.Espectaculo;
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
   // Map<String, Espectaculo> espectaculos=Fabrica.getInstance().getIEspectaculo().obtenerEspectaculos(plataforma);
    //Espectaculo espectaculo=espectaculos.get(nombre);


    //  Map <String, Espectaculo> espectaculos=Fabrica.getInstance().getIUsuario().obtenerEspectaculosArtista(usu.getNickname());
    //  request.setAttribute("tipo","Artista");
    //  request.setAttribute("espectaculos",espectaculos);
    //request.setAttribute("datos",espectaculo);
      RequestDispatcher view = request.getRequestDispatcher("/pages/espectaculo/detalle-espectaculo.jsp");
      view.forward(request, response);

    }

  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

package taller2.Espectaculo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleEspectaculo", value = "/detalle-espectaculo")
public class DetalleEspectaculo extends HttpServlet {
  Fabrica fabrica;
  
  public void init() {
    fabrica = Fabrica.getInstance();
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    String nombre = request.getParameter("nombre");
    String plataforma= request.getParameter("plataforma");
    Map<String, Espectaculo> espectaculos=Fabrica.getInstance().getIEspectaculo().obtenerEspectaculosPorPlataforma(plataforma);
    Espectaculo espectaculo=espectaculos.get(nombre);


      Map <String, Funcion> funciones=Fabrica.getInstance().getIFuncion().obtenerFuncionesDeEspectaculo(plataforma,espectaculo.getNombre());
      Map <String, Paquete> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetesDeEspectaculo(espectaculo.getNombre());
      Map <String, Categoria> categorias= Fabrica.getInstance().getICategoria().obtenerCategoriasDeEspectaculo(espectaculo.getNombre());
      request.setAttribute("categorias",categorias);
      request.setAttribute("paquetes",paquetes);
      request.setAttribute("funciones",funciones);
      request.setAttribute("datos",espectaculo);
      RequestDispatcher view = request.getRequestDispatcher("/pages/espectaculo/detalle-espectaculo.jsp");
      view.forward(request, response);

    }

  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

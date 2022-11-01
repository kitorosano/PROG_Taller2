package taller2.servlets.Espectaculo;


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
public class DetalleEspectaculoServlet extends HttpServlet {
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
    String nombre = request.getParameter("nombre");
    String plataforma= request.getParameter("plataforma");
    
    boolean espectaculoExiste = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculo(plataforma, nombre).isPresent();
    if(!espectaculoExiste) { // Si el espectaculo no existe
      request.setAttribute("respuesta","Espectaculo no encontrado");
      response.sendRedirect("listado-espectaculos");
      return;
    }
    Espectaculo espectaculo = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculo(plataforma, nombre).get();
    request.setAttribute("datos",espectaculo);

    Map<String, Funcion> funciones=Fabrica.getInstance().getIFuncion().obtenerFuncionesDeEspectaculo(plataforma,nombre);
    request.setAttribute("funciones",funciones);
    
    Map<String, Paquete> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetesDeEspectaculo(nombre, plataforma);
    request.setAttribute("paquetes",paquetes);
    
    Map<String, Categoria> categorias= Fabrica.getInstance().getICategoria().obtenerCategoriasDeEspectaculo(nombre);
    request.setAttribute("categorias",categorias);
    
    dispatchPage("/pages/espectaculo/detalle-espectaculo.jsp" , request, response);
  }

  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

package taller2.servlets.Funcion;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion;
import main.java.taller1.Logica.Clases.Funcion;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleFuncion", value = "/detalle-funcion")
public class DetalleFuncionServlet extends HttpServlet {
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
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
    String nombre = request.getParameter("nombre");
    String espectaculo = request.getParameter("espectaculo");
    String plataforma =request.getParameter("plataforma");
    
    boolean funcionExiste = Fabrica.getInstance().getIFuncion().obtenerFuncion(plataforma, espectaculo, nombre).isPresent();
    if(!funcionExiste) { // Si el espectaculo no existe
      request.setAttribute("respuesta","Funcion no encontrada");
      response.sendRedirect("listado-funciones");
      return;
    }
    Funcion funcion = Fabrica.getInstance().getIFuncion().obtenerFuncion(plataforma, espectaculo, nombre).get();
    request.setAttribute("datos",funcion);
  
    Map <String, EspectadorRegistradoAFuncion> espectadores=Fabrica.getInstance().getIFuncion().obtenerEspectadoresRegistradosAFuncion(nombre);
    request.setAttribute("espectadores",espectadores);
  
    if(esEspectador) {
      Map<String, EspectadorRegistradoAFuncion> funciones_registradas = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuarioLogueado.getNickname());
    
      if(funciones_registradas.containsKey(nombre)) {
        request.setAttribute("respuesta","Registrado a funcion");
        request.setAttribute("datosRegistro", funciones_registradas.get(nombre));
      }
    }
    
    dispatchPage("/pages/funcion/detalle-funcion.jsp" , request, response);
  }

  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

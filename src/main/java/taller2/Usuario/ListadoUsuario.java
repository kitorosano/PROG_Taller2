package taller2.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ListadoUsuario", value = "/listado-usuario")
public class ListadoUsuario extends HttpServlet {
  Fabrica fabrica;
  
  public void init() {
    fabrica = Fabrica.getInstance();
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    Map<String, Usuario> usuarios = Fabrica.getInstance().getIUsuario().obtenerUsuarios();
    request.setAttribute("usuarios", usuarios);
    RequestDispatcher view = request.getRequestDispatcher("/listado-usuario.jsp");
    Map<String, Usuario> usuarios = Fabrica.getInstance().getIUsuario().obtenerUsuarios();
    request.setAttribute("usuarios", usuarios);
    view.forward(request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

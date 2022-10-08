package taller2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;
import sun.misc.Request;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleUsuario", value = "/detalle-usuario")
public class DetalleUsuario extends HttpServlet {
  Fabrica fabrica;
  
  public void init() {
    fabrica = Fabrica.getInstance();
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    String nickname = request.getParameter("nickname");
    Map<String, Usuario> usuarios=Fabrica.getInstance().getIUsuario().obtenerUsuarios();
    Usuario usu=usuarios.get(nickname);

    if(usu instanceof Artista){
      request.setAttribute("nickname", nickname);
      request.setAttribute("nombre",usu.getNombre());
      request.setAttribute("apellido",usu.getApellido());
      request.setAttribute("correo",usu.getCorreo());
      request.setAttribute("biografia",(((Artista) usu).getBiografia()));
      request.setAttribute("fechaNac",usu.getFechaNacimiento());

      RequestDispatcher view = request.getRequestDispatcher("/detalle-artista.jsp");
      view.forward(request, response);
    }else{
      request.setAttribute("nickname", nickname);
      request.setAttribute("nombre",usu.getNombre());
      request.setAttribute("apellido",usu.getApellido());
      request.setAttribute("correo",usu.getCorreo());
      request.setAttribute("fechaNac",usu.getFechaNacimiento());

      RequestDispatcher view = request.getRequestDispatcher("/detalle-espectador.jsp");
      view.forward(request, response);
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

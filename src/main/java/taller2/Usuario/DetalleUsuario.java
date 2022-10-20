package taller2.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.EspectadorRegistradoAFuncion;
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
      Map <String, Espectaculo> espectaculos=Fabrica.getInstance().getIUsuario().obtenerEspectaculosArtista(usu.getNickname());
      request.setAttribute("tipo","Artista");
      request.setAttribute("datos",usu);
      request.setAttribute("espectaculos",espectaculos);
      RequestDispatcher view = request.getRequestDispatcher("/pages/usuario/detalle-usuario.jsp");
      view.forward(request, response);
    }else{
      Map<String, EspectadorRegistradoAFuncion> funciones=Fabrica.getInstance().getIUsuario().obtenerFuncionesRegistradasDelEspectador(usu.getNickname());
      request.setAttribute("tipo","Espectador");
      request.setAttribute("datos",usu);
      request.setAttribute("funciones",funciones);
      RequestDispatcher view = request.getRequestDispatcher("/pages/usuario/detalle-usuario.jsp");
      view.forward(request, response);
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
}

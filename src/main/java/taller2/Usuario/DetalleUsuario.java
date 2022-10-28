package taller2.Usuario;

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

@WebServlet(name = "DetalleUsuario", value = "/detalle-usuario")
public class DetalleUsuario extends HttpServlet {
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
    String nickname = request.getParameter("nickname");
    Boolean esArtista = (Boolean) session.getAttribute("esArtista");
  
    // Si el usuario no existe entonces utilizamos el nickname del usuario logueado
    if(nickname == null) {
      nickname = (String) session.getAttribute("nickname");
    }
  
    // Si el usuario no existe buscamos por correo
    Usuario usuario;
    boolean usuarioExistePorNickname = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent();
    if(!usuarioExistePorNickname) { // Si el usuario no existe
      boolean usuarioExistePorCorreo = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorCorreo(nickname).isPresent();
      if(!usuarioExistePorCorreo) {
        request.setAttribute("respuesta","Usuario no encontrado");
        System.out.println("Usuario no encontrado");
        response.sendRedirect("listado-usuarios");
        return;
      }
      usuario = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorCorreo(nickname).get();
    } else {
      usuario = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorNickname(nickname).get();
    }
    request.setAttribute("datos",usuario);
    
    // Si el usuario es artista, entonces mostramos sus espectaculos
    if(esArtista) {
      Map <String, Espectaculo> espectaculos=Fabrica.getInstance().getIEspectaculo().obtenerEspectaculosPorArtista(usuario.getNickname());
      request.setAttribute("espectaculos",espectaculos);
    }
    // Si el usuario es espectador, entonces mostramos sus funciones a las que esta registrado y sus paquetes comprados
    else {
      Map<String, EspectadorRegistradoAFuncion> funciones=Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuario.getNickname());
      request.setAttribute("funciones",funciones);
      
      Map<String, EspectadorPaquete> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(usuario.getNickname());
      request.setAttribute("paquetes",paquetes);
    }
    
    dispatchPage("/pages/usuario/detalle-usuario.jsp", request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
  }
  private boolean esFormatoCorreo(String correo){
    String regexCorreo = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
    return correo.matches(regexCorreo);
  }
}

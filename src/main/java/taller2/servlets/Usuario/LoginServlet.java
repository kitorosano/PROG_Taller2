package taller2.servlets.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectador;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;

@WebServlet(name = "Login", value = "/login")
public class LoginServlet extends HttpServlet {
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
    // Obtener el usuario de la sesion
    HttpSession session = request.getSession();
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    String message = (String) session.getAttribute("message");
    // Si hay usuario en la sesion, redirigir al home
    if(usuarioLogueado != null) {
      response.sendRedirect(request.getContextPath());
      return;
    }
    
    // Si hay mensaje de error, mostrarlo
    if(message != null) {
      request.setAttribute("message", message);
      session.removeAttribute("message");
    }
  
    dispatchPage("/pages/usuario/login.jsp", request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String nickname = request.getParameter("nickname");
    String contrasenia = request.getParameter("contrasenia");
    
    //error cuando alguno de los campos son vacios
    if(camposVacios(nickname, contrasenia)) {
      request.setAttribute("message", "Los campos obligatorios no pueden ser vacios");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/login.jsp", request, response);
      return;
    }
    
    // Buscar el usuario en la base de datos
    Usuario usuario;
    boolean usuarioExistePorNickname = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent();
    if(!usuarioExistePorNickname) { // Si el usuario no existe
      boolean usuarioExistePorCorreo = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorCorreo(nickname).isPresent();
      if(!usuarioExistePorCorreo) {
        //error cuando el usuario no existe
        request.setAttribute("message", "El usuario no existe");
        request.setAttribute("messageType", "error");
        dispatchPage("/pages/usuario/login.jsp", request, response);
        return;
      }
      usuario = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorCorreo(nickname).get();
    } else {
      usuario = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorNickname(nickname).get();
    }
    
    //error cuando la contraseña es incorrecta
    if(!usuario.getContrasenia().equals(contrasenia)) {
      request.setAttribute("message", "La contraseña es incorrecta");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/login.jsp", request, response);
      return;
    }
    
    // Si el usuario existe y la contraseña es correcta, iniciar sesión
    session.setAttribute("usuarioLogueado", usuario);
    session.setAttribute("esArtista", usuario instanceof Artista);
    session.setAttribute("esEspectador", usuario instanceof Espectador);
    response.sendRedirect("home");
  }
  
  
  // metodos para validar datos
  private boolean camposVacios(String nickname,String contrasenia){
    return nickname==null || contrasenia==null || nickname.isEmpty() || contrasenia.isEmpty();
  }
  private boolean esFormatoCorreo(String correo){
    String regexCorreo = "/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$/";
    return correo.matches(regexCorreo);
  }
}

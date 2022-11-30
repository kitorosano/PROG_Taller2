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
import main.java.taller1.Logica.DTOs.UsuarioDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;

@WebServlet(name = "Login", value = "/login")
public class LoginServlet extends HttpServlet {

  
  public void init() {
    fabrica = Fabrica.getInstance();
  }
  protected void dispatchPage(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    RequestDispatcher view = request.getRequestDispatcher(page);
    view.forward(request, response);
  }
  
  protected boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    // Si no hay sesión, redirigir a login
    if (session == null) {
      return false;
    }
    
    // Si hay sesión, obtener el usuario
    UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
    
    // Si no hay usuario, redirigir a login
    if (usuarioLogueado == null) {
      return false;
    }
    
    // Si hay usuario, enviarlo a la página de inicio
    return true;
  }
  protected void dispatchError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    request.setAttribute("message", errorMessage);
    request.setAttribute("messageType","error");
    RequestDispatcher view = request.getRequestDispatcher("/pages/usuario/login.jsp");
    view.forward(request, response);
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean sessionIniciada = checkSession(request, response);
    try {
      if(!sessionIniciada) {
        dispatchPage("/pages/usuario/login.jsp", request, response);
      } else {
        response.sendRedirect("home");
      }
    } catch (RuntimeException e) {
      dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
    }
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String nickname = request.getParameter("nickname");
    String contrasenia = request.getParameter("contrasenia");
    
    //error cuando alguno de los campos son vacios
    if(camposVacios(nickname, contrasenia)) {
      dispatchError("Los campos obligatorios no pueden ser vacios", request, response);
      return;
    }
    
    // Buscar el usuario en la base de datos
    UsuarioDTO usuario;
    
    try {
      boolean usuarioExistePorNickname = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent();
      if (!usuarioExistePorNickname) { // Si el usuario no existe
        boolean usuarioExistePorCorreo = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorCorreo(nickname).isPresent();
        if (!usuarioExistePorCorreo) {
          //error cuando el usuario no existe
          dispatchError("El usuario no existe", request, response);
          return;
        }
        usuario = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorCorreo(nickname).get();
      } else {
        usuario = Fabrica.getInstance().getIUsuario().obtenerUsuarioPorNickname(nickname).get();
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      dispatchError("Error al obtener el usuario", request, response);
      return;
    }
    
    //error cuando la contraseña es incorrecta
    if(!usuario.getContrasenia().equals(contrasenia)) {
      dispatchError("La contraseña es incorrecta", request, response);
      return;
    }
    
    // Si el usuario existe y la contraseña es correcta, iniciar sesión
    session.setAttribute("usuarioLogueado", usuario);
    session.setAttribute("esArtista", true);
    session.setAttribute("esEspectador", false);
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

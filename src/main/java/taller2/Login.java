package taller2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectador;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {
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
    String nickname = (String) session.getAttribute("nickname");
    
    // Si hay usuario en la sesion, redirigir al home
    if(nickname != null) {
      response.sendRedirect("home");
      return;
    }
  
    dispatchPage("/pages/login.jsp", request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String nickname = request.getParameter("nickname");
    String contrasenia = request.getParameter("contrasenia");
    Boolean esCorreo = request.getParameter("esCorreo").equals("true");
    
    //error cuando alguno de los campos son vacios
    if(camposVacios(nickname, contrasenia)) {
      request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
      dispatchPage("/pages/login.jsp", request, response);
      return;
    }
    
    // Buscar el usuario en la base de datos
    Usuario usuario = null;
    
    if(esCorreo) {
//      usuario = fabrica.getIUsuario().obtenerUsuarioPorCorreo(nickname);
    } else {
//      usuario = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname);
      usuario = new Espectador("Estebankito", "Esteban", "Rosano", "esteban@rosano.com", LocalDate.now(), "123", "");
  
    }
    
    //error cuando el usuario no existe
    if(usuario == null) {
      request.setAttribute("error", "El usuario no existe");
      dispatchPage("/pages/login.jsp", request, response);
      return;
    }
    
    //error cuando la contraseña es incorrecta
    if(!usuario.getContrasenia().equals(contrasenia)) {
      request.setAttribute("error", "La contraseña es incorrecta");
      dispatchPage("/pages/login.jsp", request, response);
      return;
    }
    
    // Si el usuario existe y la contraseña es correcta, iniciar sesión
    HttpSession session = request.getSession();
    session.setAttribute("nickname", usuario.getNickname());
    session.setAttribute("correo", usuario.getCorreo());
    session.setAttribute("esArtista", usuario instanceof Artista);
    session.setAttribute("esEspectador", usuario instanceof Espectador);
    response.sendRedirect("home");
  }
  
  
  // metodos para validar datos
  private boolean camposVacios(String nickname,String contrasenia){
    return nickname==null || contrasenia==null || nickname.isEmpty() || contrasenia.isEmpty();
  }
  private boolean esFormatoCorreo(String correo){
    String regexCorreo = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
    return correo.matches(regexCorreo);
  }
}

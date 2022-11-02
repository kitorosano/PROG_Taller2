package taller2.servlets.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Espectador;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet(name = "AltaUsuario", value = "/registro")
@MultipartConfig
public class AltaUsuarioServlet extends HttpServlet {

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
    dispatchPage("/pages/usuario/registro.jsp", request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String nickname = request.getParameter("nickname");
    String nombre = request.getParameter("nombre");
    String apellido = request.getParameter("apellido");
    String correo = request.getParameter("correo");
    String contrasenia = request.getParameter("contrasenia");
    String fechaNac_str = request.getParameter("fechaNac");
    String contrasenia2 = request.getParameter("contrasenia2");
    Part part=request.getPart("imagen");
    String descripcion = request.getParameter("descripcion");
    String biografia = request.getParameter("biografia");
    String url = request.getParameter("url");
    String tipo = request.getParameter("tipo");
  
    // Validar los datos traidos del formulario:
    //TODO: Pensar si vale la pena verificar el tamaño de string de los campos

    //error cuando alguno de los campos son vacios
    if(camposVacios(nickname, nombre, apellido, correo, fechaNac_str, contrasenia, contrasenia2)) {
      request.setAttribute("message", "Los campos obligatorios no pueden ser vacios");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }

    //error cuando las contraseñas no coinciden
    if(!contrasenia.equals(contrasenia2)) {
      return;
    }
    LocalDate fechaNac = LocalDate.parse(fechaNac_str); // ahora que sabemos que no es vacio, lo podemos parsear

    //error para cuando el nickname posee un formato de correo
    if(esFormatoCorreo(nickname)){
      request.setAttribute("message", "El nickname no puede tener el formato de correo");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
    if(nombreExistenteNickname(nickname)){
      request.setAttribute("message", "El nickname ingresado ya existe en la base de datos");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
    if(nombreExistenteCorreo(correo)){
      request.setAttribute("message", "El correo ingresado ya existe en la base de datos");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
    //error para cuando el correo NO posea un formato de correo
    if(!esFormatoCorreo(correo)){
      request.setAttribute("message", "Formato de correo invalido");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
    // Error contraseñas no machean
    if(!contraseniasIguales(contrasenia, contrasenia2)){
      request.setAttribute("message", "Las contraseñas no coinciden");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
    //La fecha no es valida porque no nacio mañana
    if(!fechaValida(fechaNac)){
      request.setAttribute("message", "La fecha no es valida");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
  
    String urlImagen="https://i.imgur.com/e4W1PV0.png";
    try {
      if (part.getSize() != 0) {
        InputStream inputImagen = part.getInputStream();
        urlImagen = Fabrica.getInstance().getIDatabase().guardarImagen((FileInputStream) inputImagen);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      request.setAttribute("message", "Error al guardar la imagen");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }

    // Se especifica el tipo de usuario a crear
    Usuario usuario;
    if(tipo.equals("Artista")){
      if(camposVaciosArtista(descripcion)){
        request.setAttribute("message", "Los campos obligatorios no pueden ser vacios");
        request.setAttribute("messageType", "error");
        dispatchPage("/pages/usuario/registro.jsp", request, response);
        return;
      }
      if (!url.equals("") && !esFormatoUrl(url)){
        request.setAttribute("message", "Formato de url invalida");
        request.setAttribute("messageType", "error");
        dispatchPage("/pages/usuario/registro.jsp", request, response);
      }
      
      usuario = new Artista(nickname, nombre, apellido, correo, fechaNac, contrasenia, urlImagen, descripcion, biografia, url);
    } else {
      usuario = new Espectador(nickname, nombre, apellido, correo, fechaNac, contrasenia, urlImagen);
    }

    try {
      // Se crea el usuario en la base de datos
      fabrica.getIUsuario().altaUsuario(usuario);

      // Redireccionar a la pantalla de login
      request.getSession().setAttribute("message", "Usuario creado exitosamente");
      response.sendRedirect("login"); // redirigir a otro servlet (por url)
    } catch (RuntimeException e){
      System.out.println(e.getMessage());
      // Error al crear el usuario
      request.setAttribute("message", "Error al crear el usuario");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
    }
  }


  // metodos para validar datos
  private boolean contraseniasIguales(String pass1, String pass2){
    return pass1.equals(pass2);
  }
  private boolean camposVacios(String nickname,String nombre, String apellido, String correo, String fechaNac, String contrasenia, String contrasenia2){
    return nickname==null || nombre==null || apellido==null || correo==null || fechaNac==null || contrasenia==null || contrasenia2==null ||
            nickname.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || fechaNac.isEmpty() ||  contrasenia.isEmpty() || contrasenia2.isEmpty();
  }
  private boolean esFormatoCorreo(String correo){
    String regexCorreo = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$";
    return correo.matches(regexCorreo);
  }
  private boolean camposVaciosArtista(String descripcion){
    return descripcion==null || descripcion.isEmpty();
  }
  private boolean fechaValida(LocalDate fecha){
    LocalDate hoy = LocalDate.now();
    return fecha.isEqual(hoy) || fecha.isBefore(hoy);
  }
  private boolean esFormatoUrl(String url){
    String regexURL = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
    return regexURL.matches(url);
  }

  private boolean nombreExistenteNickname(String nombreUsuario) {      //Devuelve true si hay error
    Optional<Usuario> usuario =fabrica.getIUsuario().obtenerUsuarioPorNickname(nombreUsuario);
    return usuario.isPresent();
  }

  private boolean nombreExistenteCorreo(String correo) {      //Devuelve true si hay error
    Optional<Usuario> usuario =fabrica.getIUsuario().obtenerUsuarioPorCorreo(correo);
    return usuario.isPresent();
  }

}

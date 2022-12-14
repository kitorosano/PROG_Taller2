package taller2.servlets.Usuario;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import taller2.DTOs.UsuarioDTO;
import taller2.utils.Fetch;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@WebServlet(name = "AltaUsuario", value = "/registro")
@MultipartConfig
public class AltaUsuarioServlet extends HttpServlet {
  
  Fetch fetch;
  
  public void init() {
    fetch = new Fetch();
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
    //TODO: Pensar si vale la pena verificar el tama??o de string de los campos

    //error cuando alguno de los campos son vacios
    if(camposVacios(nickname, nombre, apellido, correo, fechaNac_str, contrasenia, contrasenia2)) {
      request.setAttribute("message", "Los campos obligatorios no pueden ser vacios");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }

    //error cuando las contrase??as no coinciden
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
    // Error contrase??as no machean
    if(!contraseniasIguales(contrasenia, contrasenia2)){
      request.setAttribute("message", "Las contrase??as no coinciden");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }
    //La fecha no es valida porque no nacio ma??ana
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
        urlImagen= fetch.PostImage((FileInputStream) inputImagen).getContent();
        //urlImagen = Fabrica.getInstance().getIDatabase().guardarImagen((FileInputStream) inputImagen);
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
      request.setAttribute("message", "Error al guardar la imagen");
      request.setAttribute("messageType", "error");
      dispatchPage("/pages/usuario/registro.jsp", request, response);
      return;
    }

    // Se especifica el tipo de usuario a crear
    UsuarioDTO usuario = new UsuarioDTO();
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
        return;
      }
      
      // Crear artista
      usuario.setNickname(nickname);
      usuario.setNombre(nombre);
      usuario.setApellido(apellido);
      usuario.setCorreo(correo);
      usuario.setFechaNacimiento(fechaNac.toString());
      usuario.setContrasenia(contrasenia);
      usuario.setImagen(urlImagen);
      usuario.setDescripcion(descripcion);
      usuario.setBiografia(biografia);
      usuario.setSitioWeb(url);
      usuario.setEsArtista(true);
    } else {
      // Crear espectador
      usuario.setNickname(nickname);
      usuario.setNombre(nombre);
      usuario.setApellido(apellido);
      usuario.setCorreo(correo);
      usuario.setFechaNacimiento(fechaNac.toString());
      usuario.setContrasenia(contrasenia);
      usuario.setImagen(urlImagen);
      usuario.setEsArtista(false);
    }

    try {
      // Se envia el usuario en la base de datos
      fetch.Set("/usuarios/create", usuario).Post();
      //fabrica.getIUsuario().altaUsuario(usuario);

      // Redireccionar a la pantalla de login
      request.getSession().setAttribute("message", "Usuario creado exitosamente");
      request.setAttribute("messageType", "success");
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
    //TODO: Arreglar esto
    String pattern = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
    return url.matches(pattern);
  }

  private boolean nombreExistenteNickname(String nombreUsuario) {      //Devuelve true si hay error
    try {
      UsuarioDTO usuario = fetch.Set("/usuarios/findByNickname?nickname="+nombreUsuario).Get().getUsuario();
      return usuario != null;
    } catch (RuntimeException e) {
      return false;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean nombreExistenteCorreo(String correo) {      //Devuelve true si hay error
    try {
      UsuarioDTO usuario = fetch.Set("/usuarios/findByCorreo?correo="+correo).Get().getUsuario();
      return usuario != null;
    } catch (RuntimeException | IOException e) {
      throw new RuntimeException(e);
    }
  }

}

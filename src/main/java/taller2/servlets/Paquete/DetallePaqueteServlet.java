package taller2.servlets.Paquete;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response;
import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet(name = "DetallePaquete", value = "/detalle-paquete")
public class DetallePaqueteServlet extends HttpServlet {
  
  Fetch fetch;
  
  public void init() {
    fetch = new Fetch();
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
    RequestDispatcher view = request.getRequestDispatcher("/pages/index.jsp");
    view.forward(request, response);
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Si no hay sesión, redirigir a login
    boolean sessionIniciada = checkSession(request, response);
    try {
      if(sessionIniciada) {
        Map<String, PlataformaDTO> todasPlataformas =  fetch.Set("/plataformas/findAll").Get().getMapPlataforma();
        Map<String, EspectaculoDTO> todosEspectaculos =  fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
        Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll/").Get().getMapPaquete();
        Map<String, CategoriaDTO> todasCategorias = fetch.Set("/categorias/findAll/").Get().getMapCategoria();
        Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll/").Get().getMapUsuario();
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
        request.setAttribute("message","No es null");   //Para que al comparar en el jsp no sea null
      
        HttpSession session = request.getSession();
        Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
        UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        String nombre = request.getParameter("nombre");
    
        //boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
        PaqueteDTO paqueteExiste = fetch.Set("/paquetes/find?nombre="+nombre).Get().getPaquete();
        if(paqueteExiste==null) { // Si el paquete no existe
          request.setAttribute("message","Paquete no encontrado");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-paquetes");
          return;
        }
        //PaqueteDTO paquete = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).get();
        PaqueteDTO paquete = fetch.Set("/paquetes/find?nombre="+nombre).Get().getPaquete();
        request.setAttribute("datos",paquete);
        
        //Map<String, EspectaculoDTO> espectaculos = Fabrica.getInstance().getIPaquete().obtenerEspectaculosDePaquete(nombre);
        Map<String, EspectaculoDTO> espectaculos = fetch.Set("/espectaculos/findByPaquete?nombrePaquete="+nombre).Get().getMapEspectaculo();
        request.setAttribute("espectaculos",espectaculos);
        
        if(esEspectador) {
            //Map<String, EspectadorPaqueteDTO> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(usuarioLogueado.getNickname());
          Map<String, PaqueteDTO> paquetes_espectador = fetch.Set("/paquetes/findByNombreEspectador?nombreEspectador="+usuarioLogueado.getNickname()).Get().getMapPaquete();
            if(paquetes_espectador.containsKey(nombre)) {
                request.setAttribute("message","Paquete Adquirido");
                request.setAttribute("fechaCompra", paquetes_espectador.get(nombre).getFechaRegistro());
            }
        }
        
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
      } else {
        response.sendRedirect("login");
      }
    } catch (RuntimeException e) {
      dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    String nombre = request.getParameter("nombre");
    //String nickname_espectador = (String) session.getAttribute("nickname");
    UsuarioDTO usuSession=(UsuarioDTO)session.getAttribute("usuarioLogueado");
    String nickname_espectador= usuSession.getNickname();
    //boolean paqueteExiste = Fabrica.getInstance().getIPaquete().obtenerPaquete(nombre).isPresent();
    PaqueteDTO paqueteExiste = fetch.Set("/paquetes/find?nombre="+nombre).Get().getPaquete();
    if(paqueteExiste==null) { // Si el paquete no existe
        request.setAttribute("message","Paquete no encontrado");
        request.setAttribute("messageType","error");
        dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
        return;
    }
  
    //Map<String, EspectadorPaqueteDTO> paquetes_espectador = Fabrica.getInstance().getIPaquete().obtenerPaquetesPorEspectador(nickname_espectador);
    Map<String, PaqueteDTO> paquetes_espectador = fetch.Set("/paquetes/findByNombreEspectador?nombreEspectador="+nickname_espectador).Get().getMapPaquete();
    boolean paqueteYaComprado = paquetes_espectador.containsKey(nombre); // Si el paquete no está comprado, paquete_comprado es null

    if (paqueteYaComprado) {
      request.setAttribute("message", "Paquete ya está comprado");
      request.setAttribute("messageType","error");
      dispatchPage("/pages/paquete/detalle-paquete.jsp", request, response);
      return;
    }
    
    //Fabrica.getInstance().getIPaquete().altaEspectadorAPaquete(nombre, nickname_espectador);
    AltaEspectadorAPaqueteDTO alta_espectador_a_paquete = new AltaEspectadorAPaqueteDTO();
    alta_espectador_a_paquete.setNombrePaquete(nombre);
    alta_espectador_a_paquete.setNickname(nickname_espectador);
    alta_espectador_a_paquete.setFechaRegistro(LocalDateTime.now().toString());

    fetch.Set("/paquetes/createEspectadorAPaquete",alta_espectador_a_paquete).Post();
    request.setAttribute("message", "Paquete Adquirido");
    response.sendRedirect("detalle-paquete" + "?nombre=" + nombre);
  }
}

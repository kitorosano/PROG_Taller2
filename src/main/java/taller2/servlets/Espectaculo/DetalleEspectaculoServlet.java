package taller2.servlets.Espectaculo;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import taller2.DTOs.*;
import taller2.E_EstadoEspectaculo;
import taller2.utils.Fetch;


import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleEspectaculo", value = "/detalle-espectaculo")
public class DetalleEspectaculoServlet extends HttpServlet {
  
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
                
        Map<String, PlataformaDTO> todasPlataformas = fetch.Set("/plataformas/findAll").Get().getMapPlataforma();
        Map<String, EspectaculoDTO> todosEspectaculos = fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
        Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll").Get().getMapPaquete();
        Map<String, CategoriaDTO> todasCategorias = fetch.Set("/categorias/findAll").Get().getMapCategoria();
        Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll").Get().getMapUsuario();
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
    
        String nombre = request.getParameter("nombre");
        String plataforma= request.getParameter("plataforma");

        HttpSession session = request.getSession();
        Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
        String usuarioLogueadoNickname = ((UsuarioDTO) session.getAttribute("usuarioLogueado")).getNickname();
        
        //boolean espectaculoExiste = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculo(plataforma, nombre).isPresent();
        EspectaculoDTO espectaculo = fetch.Set("/espectaculos/find?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).Get().getEspectaculo();
        if(espectaculo==null) { // Si el espectaculo no existe
          request.setAttribute("message","Espectaculo no encontrado");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-espectaculos");
          return;
        }
        request.setAttribute("datos",espectaculo);

        Map<String, FuncionDTO> funciones=  fetch.Set("/funciones/findByEspectaculoAndPlataforma?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).Get().getMapFuncion();
        request.setAttribute("funciones",funciones);

        Map<String, PaqueteDTO> paquetes= fetch.Set("/paquetes/findByEspectaculoAndPlataforma?nombreEspectaculo="+nombre+"&nombrePlataforma="+plataforma).Get().getMapPaquete();
        request.setAttribute("paquetes",paquetes);

        Map<String, CategoriaDTO> categorias= fetch.Set("/categorias/findByEspectaculoAndPlataforma?nombreEspectaculo="+nombre+"&nombrePlataforma="+plataforma).Get().getMapCategoria();
        request.setAttribute("categorias",categorias);

        Map<String, String> espectaculosFavoritos = fetch.Set("/usuarios/findEspectaculosFavoritos?nickname="+usuarioLogueadoNickname).Get().getMapEspectaculosFavoritos();
        request.setAttribute("espectaculosFavoritos", espectaculosFavoritos);
        
        dispatchPage("/pages/espectaculo/detalle-espectaculo.jsp" , request, response);
      } else {
        response.sendRedirect("login");
      }
    } catch (RuntimeException e) {
      dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
    }
  }

  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      boolean tipoAccion = Boolean.parseBoolean(request.getParameter("accion"));

      EspectaculoFavoritoDTO dto = new EspectaculoFavoritoDTO();

      String nickname = request.getParameter("nickname");
      String nombreEspectaculo = request.getParameter("nombreEspectaculo");
      String nombrePlataforma = request.getParameter("nombrePlataforma");

      dto.setNickname(nickname);
      dto.setNombreEspectaculo(nombreEspectaculo);
      dto.setNombrePlataforma(nombrePlataforma);
    System.out.println("el espectaculo es"+dto);
      if (tipoAccion == true) {
        fetch.Set("/usuarios/createEspectaculoFavorito", dto).Post();
      } else {
        fetch.Set("/usuarios/deleteEspectaculoFavorito", dto).Delete();
      }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String nombreEspectaculo = (String)request.getParameter("nombre");
    String nombrePlataforma = (String)request.getParameter("plataforma");
    EspectaculoNuevoEstadoDTO espectaculo = new EspectaculoNuevoEstadoDTO();
    System.out.println("el nombre del espectaculo es:"+nombreEspectaculo);
    System.out.println("la plataforma es:"+nombrePlataforma);
   // EspectaculoDTO espectaculo = fetch.Set("/espectaculos/find?nombrePlataforma="+nombrePlataforma+"&nombreEspectaculo="+nombreEspectaculo).Get().getEspectaculo();
    espectaculo.setNombreEspectaculo(nombreEspectaculo);
    espectaculo.setNombrePlataforma(nombrePlataforma);
    espectaculo.setNuevoEstado(E_EstadoEspectaculo.FINALIZADO);
    System.out.println("el espectaculo es"+espectaculo);
    System.out.println("el nuevo estado es:"+E_EstadoEspectaculo.FINALIZADO);
    fetch.Set("/espectaculos/updateEstado",espectaculo).Put();
  }
}

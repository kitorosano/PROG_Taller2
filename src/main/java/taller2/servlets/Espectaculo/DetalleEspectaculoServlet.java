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
                
        Map<String, PlataformaDTO> todasPlataformas = fetch.Set("/plataformas/findAll").Get().getContentMap(PlataformaDTO.class);
        Map<String, EspectaculoDTO> todosEspectaculos = fetch.Set("/espectaculos/findAll").Get().getContentMap(EspectaculoDTO.class);
        Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll").Get().getContentMap(PaqueteDTO.class);
        Map<String, CategoriaDTO> todasCategorias  = fetch.Set("/categorias/findAll").Get().getContentMap(CategoriaDTO.class);
        Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll").Get().getContentMap(UsuarioDTO.class);
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
    
        String nombre = request.getParameter("nombre");
        String plataforma= request.getParameter("plataforma");
        
        //boolean espectaculoExiste = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculo(plataforma, nombre).isPresent();
        EspectaculoDTO espectaculo = fetch.Set("/espectaculos/find?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).Get().getContent(EspectaculoDTO.class);
        if(espectaculo==null) { // Si el espectaculo no existe
          request.setAttribute("message","Espectaculo no encontrado");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-espectaculos");
          return;
        }
        request.setAttribute("datos",espectaculo);
    
        //Map<String, FuncionDTO> funciones=Fabrica.getInstance().getIFuncion().obtenerFuncionesDeEspectaculo(plataforma,nombre);

        Map<String, FuncionDTO> funciones=  fetch.Set("/funciones/findByEspectaculoAndPlataforma?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).Get().getContentMap(FuncionDTO.class);
        request.setAttribute("funciones",funciones);
        
        //Map<String, PaqueteDTO> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetesDeEspectaculo(nombre, plataforma);

        Map<String, PaqueteDTO> paquetes= fetch.Set("/paquetes/findByspectaculoAndPlataforma?nombreEspectaculo="+nombre+"&nombrePlataforma="+plataforma).Get().getContentMap(PaqueteDTO.class);
        request.setAttribute("paquetes",paquetes);
        
        //Map<String, CategoriaDTO> categorias= Fabrica.getInstance().getICategoria().obtenerCategoriasDeEspectaculo(nombre);

        Map<String, CategoriaDTO> categorias= fetch.Set("/categorias/findByEspectaculo?nombreEspectaculo="+nombre+"&nombrePlataforma="+plataforma).Get().getContentMap(CategoriaDTO.class);
        request.setAttribute("categorias",categorias);
        
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

      dto.setNickname(request.getParameter("nickname"));
      dto.setNombreEspectaculo(request.getParameter("nombreEspectaculo"));
      dto.setNombrePlataforma(request.getParameter("nombrePlataforma"));

      if (tipoAccion == true) {
        fetch.Set("/usuarios/createEspectaculoFavorito", dto).Post();
      } else {
        fetch.Set("/usuarios/deleteEspectaculoFavorito", dto).Delete();
      }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String nombreEspectaculo = (String)request.getParameter("nombreEspectaculo");
    String nombrePlataforma = (String)request.getParameter("nombrePlataforma");

    EspectaculoDTO espectaculo = fetch.Set("/espectaculos/find?nombrePlataforma="+nombrePlataforma+"&nombreEspectaculo="+nombreEspectaculo).Get().getContent(EspectaculoDTO.class);
    espectaculo.setEstado(E_EstadoEspectaculo.FINALIZADO);
    fetch.Set("espectaculo/updateEstado",espectaculo).Post();
  }
}

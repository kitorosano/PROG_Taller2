package taller2.servlets.Espectaculo;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import taller2.DTOs.*;
import taller2.utils.Utils;


import java.io.IOException;
import java.util.Map;

@WebServlet(name = "DetalleEspectaculo", value = "/detalle-espectaculo")
public class DetalleEspectaculoServlet extends HttpServlet {


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
        Map<String, PlataformaDTO> todasPlataformas = (Map<String, PlataformaDTO>) Utils.FetchApi("/plataformas").getEntity();
        Map<String, EspectaculoDTO> todosEspectaculos = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos").getEntity();
        Map<String, PaqueteDTO> todosPaquetes = (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes").getEntity();
        Map<String, CategoriaDTO> todasCategorias  = (Map<String, CategoriaDTO>) Utils.FetchApi("/categorias").getEntity();
        Map<String, UsuarioDTO> todosUsuarios = (Map<String, UsuarioDTO>) Utils.FetchApi("/usuarios").getEntity();
      
        request.setAttribute("todasPlataformas", todasPlataformas);
        request.setAttribute("todosEspectaculos", todosEspectaculos);
        request.setAttribute("todosPaquetes", todosPaquetes);
        request.setAttribute("todasCategorias", todasCategorias);
        request.setAttribute("todosUsuarios", todosUsuarios);
    
        String nombre = request.getParameter("nombre");
        String plataforma= request.getParameter("plataforma");
        
        //boolean espectaculoExiste = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculo(plataforma, nombre).isPresent();
        EspectaculoDTO espect = (EspectaculoDTO) Utils.FetchApi("/espectaculos/?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).getEntity();
        if(espect==null) { // Si el espectaculo no existe
          request.setAttribute("message","Espectaculo no encontrado");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-espectaculos");
          return;
        }
        //EspectaculoDTO espectaculo = Fabrica.getInstance().getIEspectaculo().obtenerEspectaculo(plataforma, nombre).get();

        EspectaculoDTO espectaculo = (EspectaculoDTO) Utils.FetchApi("/espectaculos/?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).getEntity();
        request.setAttribute("datos",espectaculo);
    
        //Map<String, FuncionDTO> funciones=Fabrica.getInstance().getIFuncion().obtenerFuncionesDeEspectaculo(plataforma,nombre);

        Map<String, FuncionDTO> funciones=(Map<String, FuncionDTO>) Utils.FetchApi("/funciones/?nombrePlataforma="+plataforma+"&nombreEspectaculo="+nombre).getEntity();
        request.setAttribute("funciones",funciones);
        
        //Map<String, PaqueteDTO> paquetes=Fabrica.getInstance().getIPaquete().obtenerPaquetesDeEspectaculo(nombre, plataforma);

        Map<String, PaqueteDTO> paquetes=(Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes/?nombreEspectaculo="+nombre+"&nombrePlataforma="+plataforma).getEntity();
        request.setAttribute("paquetes",paquetes);
        
        //Map<String, CategoriaDTO> categorias= Fabrica.getInstance().getICategoria().obtenerCategoriasDeEspectaculo(nombre);

        Map<String, CategoriaDTO> categorias=(Map<String, CategoriaDTO>) Utils.FetchApi("/categorias/?nombreEspectaculo="+nombre).getEntity();
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
  
  }
}

package taller2.servlets.Funcion;

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

@WebServlet(name = "DetalleFuncion", value = "/detalle-funcion")
public class DetalleFuncionServlet extends HttpServlet {

  

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
      
        HttpSession session = request.getSession();
        UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        Boolean esEspectador = (Boolean) session.getAttribute("esEspectador");
        String nombre = request.getParameter("nombre");
        String espectaculo = request.getParameter("espectaculo");
        String plataforma =request.getParameter("plataforma");

        //boolean funcionExiste = Fabrica.getInstance().getIFuncion().obtenerFuncion(plataforma, espectaculo, nombre).isPresent();
        FuncionDTO funcionExiste = (FuncionDTO) Utils.FetchApi("/funciones/?nombrePlataforma="+plataforma+"&nombreEspectaculo="+espectaculo+"&nombreFuncion="+nombre).getEntity();
        if(funcionExiste==null) { // Si el espectaculo no existe
          request.setAttribute("message","Funcion no encontrada");
          request.setAttribute("messageType","error");
          response.sendRedirect("listado-funciones");
          return;
        }
        //FuncionDTO funcion = Fabrica.getInstance().getIFuncion().obtenerFuncion(plataforma, espectaculo, nombre).get();
        FuncionDTO funcion = (FuncionDTO) Utils.FetchApi("/funciones/?nombrePlataforma="+plataforma+"&nombreEspectaculo="+espectaculo+"&nombreFuncion="+nombre).getEntity();
        request.setAttribute("datos",funcion);
      
        //Map <String, EspectadorRegistradoAFuncionDTO> espectadores=Fabrica.getInstance().getIFuncion().obtenerEspectadoresRegistradosAFuncion(nombre);
        Map <String, EspectadorRegistradoAFuncionDTO> espectadores= (Map <String, EspectadorRegistradoAFuncionDTO>) Utils.FetchApi("/EspectadorAFuncion/?nombre="+nombre).getEntity();
        request.setAttribute("espectadores",espectadores);
      
        if(esEspectador) {
          //Map<String, EspectadorRegistradoAFuncionDTO> funciones_registradas = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(usuarioLogueado.getNickname());
          Map<String, EspectadorRegistradoAFuncionDTO> funciones_registradas = (Map <String, EspectadorRegistradoAFuncionDTO>) Utils.FetchApi("/EspectadorAFuncion/?nicknameEspectador="+usuarioLogueado.getNickname()).getEntity();
          if(funciones_registradas.containsKey(nombre)) {
            request.setAttribute("message","Registrado a funcion");
            request.setAttribute("datosRegistro", funciones_registradas.get(nombre));
          }
        }
        
        dispatchPage("/pages/funcion/detalle-funcion.jsp" , request, response);
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

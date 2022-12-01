package taller2.servlets.Funcion;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ListadoFunciones", value = "/listado-funciones")
public class ListadoFuncionesServlet extends HttpServlet {
  
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
                Map<String, PlataformaDTO> todasPlataformas = fetch.Set("/plataformas/findAll/").Get().getContentMap(PlataformaDTO.class);
                Map<String, EspectaculoDTO> todosEspectaculos = fetch.Set("/espectaculos/findAll/").Get().getContentMap(EspectaculoDTO.class);
                Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll/").Get().getContentMap(PaqueteDTO.class);
                Map<String, CategoriaDTO> todasCategorias  = fetch.Set("/categorias/findAll/").Get().getContentMap(CategoriaDTO.class);
                Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll/").Get().getContentMap(UsuarioDTO.class);
    
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
                
                String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
                String filtroEspectaculo = request.getParameter("filtroEspectaculo") != null ? request.getParameter("filtroEspectaculo") : "";
                Map<String, FuncionDTO> funcionesFiltradas;
                
                // Si se llega con un filtrado vacio
                if(filtroPlataforma.isEmpty() && filtroEspectaculo.isEmpty()) {
                    funcionesFiltradas = fetch.Set("/funciones/findAll/").Get().getContentMap(FuncionDTO.class);
                    request.setAttribute("funcionesFiltradas", funcionesFiltradas);
                }
                // Si se llega con un filtrado de plataforma
                else if (!filtroPlataforma.isEmpty() && filtroEspectaculo.isEmpty()) {
                    //funcionesFiltradas = fabrica.getIFuncion().obtenerFuncionesDePlataforma(filtroPlataforma);
                    funcionesFiltradas = fetch.Set("/funciones/findByPlataforma?nombrePlataforma="+filtroPlataforma).Get().getContentMap(FuncionDTO.class);
                    request.setAttribute("funcionesFiltradas", funcionesFiltradas);
                }
                // Si llega con un filtrado espectaculo tambien llegara con uno de plataforma
                else {
                    //funcionesFiltradas = fabrica.getIFuncion().obtenerFuncionesDeEspectaculo(filtroPlataforma, filtroEspectaculo);
                    funcionesFiltradas = fetch.Set("/funciones/findByEspectaculoAndPlataforma?nombrePlataforma="+filtroPlataforma+"&nombreEspectaculo="+filtroEspectaculo).Get().getContentMap(FuncionDTO.class);
                    request.setAttribute("funcionesFiltradas", funcionesFiltradas);
                }
                
                dispatchPage("/pages/funcion/listado-funciones.jsp", request, response);
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

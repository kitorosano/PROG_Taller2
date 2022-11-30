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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ListadoEspectaculos", value = "/listado-espectaculos")
public class ListadoEspectaculosServlet extends HttpServlet {

    

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
                String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
                String filtroCategoria = request.getParameter("filtroCategoria") != null ? request.getParameter("filtroCategoria") : "";
                Map<String, EspectaculoDTO> espectaculosFiltrados = new HashMap<>();
                Map<String, Map<String, CategoriaDTO>> categoriasEspectaculosFiltrados = new HashMap<>();
    
                // Si se llega con un filtrado vacio
                if(filtroPlataforma.isEmpty() && filtroCategoria.isEmpty()) {
                    espectaculosFiltrados = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos").getEntity();
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                // Si se llega con un filtrado de plataforma
                else if (!filtroPlataforma.isEmpty() && filtroCategoria.isEmpty()) {
                    //espectaculosFiltrados = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(filtroPlataforma);

                    espectaculosFiltrados = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos/?nombrePlataforma="+filtroPlataforma).getEntity();
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                // Si se llega con un filtrado de categoria
                else if (filtroPlataforma.isEmpty() && !filtroCategoria.isEmpty()) {
                    //espectaculosFiltrados = fabrica.getICategoria().obtenerEspectaculosDeCategoria(filtroCategoria);

                    espectaculosFiltrados = (Map<String, EspectaculoDTO>) Utils.FetchApi("/categorias/?nombre="+filtroCategoria).getEntity();
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                // Si llega con un filtrado de plataforma y categoria
                else {
                    //Map<String, EspectaculoDTO> espectaculosDePlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(filtroPlataforma);
                    Map<String, EspectaculoDTO> espectaculosDePlataforma = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos/?nombrePlataforma="+filtroPlataforma).getEntity();

                    //Map<String, EspectaculoDTO> espectaculosDeCategoria = fabrica.getICategoria().obtenerEspectaculosDeCategoria(filtroCategoria);
                    Map<String, EspectaculoDTO> espectaculosDeCategoria = (Map<String, EspectaculoDTO>) Utils.FetchApi("/categorias/?nombre="+filtroCategoria).getEntity();
                
                    for (EspectaculoDTO espectaculo : espectaculosDeCategoria.values()){
                        if (espectaculosDePlataforma.containsKey(espectaculo.getNombre())){
                            espectaculosFiltrados.put(espectaculo.getNombre(), espectaculo);
                        }
                    }
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                
                // Cargar categorias de los espectaculos filtrados
                for (EspectaculoDTO espectaculo : espectaculosFiltrados.values()){
                    //Map<String,CategoriaDTO> categoriasEspectaculoFiltrado = fabrica.getICategoria().obtenerCategoriasDeEspectaculo(espectaculo.getNombre());
                    Map<String,CategoriaDTO> categoriasEspectaculoFiltrado = (Map<String, CategoriaDTO>) Utils.FetchApi("/categorias/?nombreEspectaculo="+espectaculo.getNombre()).getEntity();
                    categoriasEspectaculosFiltrados.put(espectaculo.getNombre(), categoriasEspectaculoFiltrado);
                }
                request.setAttribute("categoriasEspectaculosFiltrados", categoriasEspectaculosFiltrados);
                
                dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);
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

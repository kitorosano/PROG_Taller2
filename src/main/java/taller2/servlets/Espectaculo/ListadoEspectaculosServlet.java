package taller2.servlets.Espectaculo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.PaqueteDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ListadoEspectaculos", value = "/listado-espectaculos")
public class ListadoEspectaculosServlet extends HttpServlet {
    Fabrica fabrica;
    
    public void init() {
        fabrica = Fabrica.getInstance();
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
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
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
                Map<String, Plataforma> todasPlataformas = fabrica.getIPlataforma().obtenerPlataformas();
                Map<String, Espectaculo> todosEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
                Map<String, PaqueteDTO> todosPaquetes = fabrica.getIPaquete().obtenerPaquetes();
                Map<String, Categoria> todasCategorias = fabrica.getICategoria().obtenerCategorias();
                Map<String, Usuario> todosUsuarios = fabrica.getIUsuario().obtenerUsuarios();
            
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
                String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
                String filtroCategoria = request.getParameter("filtroCategoria") != null ? request.getParameter("filtroCategoria") : "";
                Map<String, Espectaculo> espectaculosFiltrados = new HashMap<>();
                Map<String, Map<String, Categoria>> categoriasEspectaculosFiltrados = new HashMap<>();
    
                // Si se llega con un filtrado vacio
                if(filtroPlataforma.isEmpty() && filtroCategoria.isEmpty()) {
                    espectaculosFiltrados = fabrica.getIEspectaculo().obtenerEspectaculos();
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                // Si se llega con un filtrado de plataforma
                else if (!filtroPlataforma.isEmpty() && filtroCategoria.isEmpty()) {
                    espectaculosFiltrados = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(filtroPlataforma);
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                // Si se llega con un filtrado de categoria
                else if (filtroPlataforma.isEmpty() && !filtroCategoria.isEmpty()) {
                    espectaculosFiltrados = fabrica.getICategoria().obtenerEspectaculosDeCategoria(filtroCategoria);
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                // Si llega con un filtrado de plataforma y categoria
                else {
                    Map<String, Espectaculo> espectaculosDePlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(filtroPlataforma);
                    Map<String, Espectaculo> espectaculosDeCategoria = fabrica.getICategoria().obtenerEspectaculosDeCategoria(filtroCategoria);
                
                    for (Espectaculo espectaculo : espectaculosDeCategoria.values()){
                        if (espectaculosDePlataforma.containsKey(espectaculo.getNombre())){
                            espectaculosFiltrados.put(espectaculo.getNombre(), espectaculo);
                        }
                    }
                    request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
                }
                
                // Cargar categorias de los espectaculos filtrados
                for (Espectaculo espectaculo : espectaculosFiltrados.values()){
                    Map<String,Categoria> categoriasEspectaculoFiltrado = fabrica.getICategoria().obtenerCategoriasDeEspectaculo(espectaculo.getNombre());
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

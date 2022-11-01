package taller2.servlets.Espectaculo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.taller1.Logica.Clases.Categoria;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Plataforma;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
        String filtroCategoria = request.getParameter("filtroCategoria") != null ? request.getParameter("filtroCategoria") : "";
        Map<String, Plataforma> plataformas;
        Map<String, Categoria> categorias;
        Map<String, Espectaculo> espectaculosFiltrados = new HashMap<>();
        Map<String, Map<String, Categoria>> categoriasEspectaculosFiltrados = new HashMap<>();
    
        // Cargar opciones plataformas y categorias para el filtrado
        plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);
    
        categorias = fabrica.getICategoria().obtenerCategorias();
        request.setAttribute("categorias", categorias);
    
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}

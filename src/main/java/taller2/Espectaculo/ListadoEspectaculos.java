package taller2.Espectaculo;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(name = "ListadoEspectaculos", value = "/listado-espectaculos")
public class ListadoEspectaculos extends HttpServlet {
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
        String miCategoria = request.getParameter("categoria");
        String miPlataforma = request.getParameter("plataforma");
        Map<String, Plataforma> plataformas;
        Map<String, Espectaculo> totalEspectaculos;
        Map<String, Categoria> categorias;
    
        // Si se llega con un filtrado vacio
        if(miPlataforma == null || miCategoria == null){
            plataformas = fabrica.getIPlataforma().obtenerPlataformas();
            request.setAttribute("plataformas", plataformas);
        
            categorias = fabrica.getICategoria().obtenerCategorias();
            request.setAttribute("categorias", categorias);
    
            totalEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
            request.setAttribute("totalEspectaculos", totalEspectaculos);
        
            dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);
            return;
        }
    
        // Si se llego con algo filtrado
        if (!miPlataforma.equals("Todas") && !miCategoria.equals("Todas")){
            Map<String, Espectaculo> espectaculosPlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);
            Map<String, Espectaculo> espectaculosDeCategoria = fabrica.getICategoria().obtenerEspectaculosDeCategoria(miCategoria);
            Map<String, Espectaculo> espectaculosPlataformaCategoria = new HashMap<>();
        
            for (Espectaculo e : espectaculosDeCategoria.values()){
                if (espectaculosPlataforma.containsKey(e.getNombre())){
                    espectaculosPlataformaCategoria.put(e.getNombre(), e);
                }
            }
        
            request.setAttribute("plataforma", miPlataforma);
            request.setAttribute("categoria", miCategoria);
            request.setAttribute("espectaculosPlataformaCategoria", espectaculosPlataformaCategoria);
        
            dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);
        
        } else if (!miPlataforma.equals("Todas")) {
            Map<String, Espectaculo> espectaculosDePlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);
            request.setAttribute("espectaculosDePlataforma", espectaculosDePlataforma);
        
        } else if (!miCategoria.equals("Todas")){
            Map<String, Espectaculo> espectaculosDeCategoria= fabrica.getICategoria().obtenerEspectaculosDeCategoria(miCategoria);
            request.setAttribute("espectaculosDeCategoria", espectaculosDeCategoria);
        
        } else {
            totalEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
            request.setAttribute("totalEspectaculos", totalEspectaculos);
        
        }
    
        dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    }
    
}

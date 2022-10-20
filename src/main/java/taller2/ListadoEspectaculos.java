package taller2;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Clases.Usuario;
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
        Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        Map<String, Espectaculo> totalEspectaculos = new HashMap();

        for (Plataforma p : plataformas.values()) {
            Map<String, Espectaculo> auxEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(p.getNombre());
            for (Espectaculo e : auxEspectaculos.values()) {
                totalEspectaculos.put(e.getNombre(), e);
            }
        }
        request.setAttribute("plataformas", plataformas);
        request.setAttribute("totalEspectaculos", totalEspectaculos);
        dispatchPage("/pages/listado-espectaculos.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String miPlataforma = request.getParameter("plataforma");
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);
        request.setAttribute("espectaculos", espectaculos);

        Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);

        dispatchPage("/pages/listado-espectaculos.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
    }
}

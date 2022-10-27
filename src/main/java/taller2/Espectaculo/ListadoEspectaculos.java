package taller2.Espectaculo;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Categoria;
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
        Map<String, Categoria> categorias = fabrica.getICategoria().obtenerCategorias();

        totalEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();

        request.setAttribute("plataformas", plataformas);
        request.setAttribute("categorias", categorias);
        request.setAttribute("totalEspectaculos", totalEspectaculos);
        dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);

        Map<String, Categoria> categorias = fabrica.getICategoria().obtenerCategorias();
        request.setAttribute("categorias", categorias);

        if (!request.getParameter("plataforma").equals("Todas") && !request.getParameter("categoria").equals("Todas")){
            String miPlataforma = request.getParameter("plataforma");
            Map<String, Espectaculo> espectaculosPlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);

            String miCategoria = request.getParameter("categoria");
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

        } else if (!request.getParameter("plataforma").equals("Todas")) {

            String miPlataforma = request.getParameter("plataforma");
            Map<String, Espectaculo> espectaculosDePlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);
            request.setAttribute("espectaculosDePlataforma", espectaculosDePlataforma);

        } else if (!request.getParameter("categoria").equals("Todas")){

            String miCategoria = request.getParameter("categoria");
            Map<String, Espectaculo> espectaculosDeCategoria= fabrica.getICategoria().obtenerEspectaculosDeCategoria(miCategoria);
            request.setAttribute("espectaculosDeCategoria", espectaculosDeCategoria);

        } else {
            System.out.println("HOLA VOY A GUARDAR TODA LA TABLA");
            Map<String, Espectaculo> totalEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
            request.setAttribute("totalEspectaculos", totalEspectaculos);

        }

        dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);

    }


}

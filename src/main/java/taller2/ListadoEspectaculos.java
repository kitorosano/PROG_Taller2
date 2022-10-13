package taller2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
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
        Map<String, Plataforma> plataformas = fabrica.getIEspectaculo().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);
        dispatchPage("/pages/listado-espectaculos.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String miPlataforma = request.getParameter("plataforma");
        System.out.println(miPlataforma);
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos(miPlataforma);
        request.setAttribute("espectaculos", espectaculos);

        Map<String, Plataforma> plataformas = fabrica.getIEspectaculo().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);


        dispatchPage("/pages/listado-espectaculos.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
    }
}

package taller2.Funcion;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Funcion;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ListadoFunciones", value = "/listado-funciones")
public class ListadoFunciones extends HttpServlet {
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
        request.setAttribute("plataformas", plataformas);

        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
        request.setAttribute("espectaculos", espectaculos);

        Map<String, Funcion> funciones = fabrica.getIFuncion().obtenerFunciones();
        request.setAttribute("funciones", funciones);

        dispatchPage("/pages/funcion/listado-funciones.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String miPlataforma = request.getParameter("plataforma");
        String miEspectaculo = request.getParameter("espectaculo");

        Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);

        Map<String, Espectaculo> espectaculos;

        if (miPlataforma.equals("Todas")) {
            espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
        } else {
            espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);
        }
        request.setAttribute("espectaculos", espectaculos);

        if (request.getParameter("tipoPost") == "tipo2"){
            dispatchPage("/pages/funcion/listado-funciones.jsp", request, response);
        }

        Map<String, Funcion> funciones = new HashMap<>();
        if (miEspectaculo.equals("Todos")){
            funciones = fabrica.getIFuncion().obtenerFunciones();
        } else {
            funciones = fabrica.getIFuncion().obtenerFuncionesDeEspectaculo(miPlataforma, miEspectaculo);
        }

        request.setAttribute("funciones", funciones);
        dispatchPage("/pages/funcion/listado-funciones.jsp", request, response);

    }


}

    /*
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String miPlataforma = request.getParameter("plataforma");

        Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);

        if (request.getParameter("espectaculo") == null){
            Map<String, Espectaculo> espectaculos;

            if (miPlataforma.equals("Todas")){
                System.out.println("ANTESSSSS");
                espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
                System.out.println("DESPUEEEES");

            } else {
                espectaculos = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(miPlataforma);
            }

            request.setAttribute("espectaculos", espectaculos);
        }

        String miEspectaculo = request.getParameter("espectaculo");
        Map<String, Funcion> funciones = new HashMap<>();

        if (miEspectaculo.equals("Todos")){
            funciones = fabrica.getIFuncion().obtenerFunciones();
        } else {
            funciones = fabrica.getIFuncion().obtenerFuncionesDeEspectaculo(miPlataforma, miEspectaculo);
        }

        request.setAttribute("funciones", funciones);
        dispatchPage("/pages/funcion/listado-funciones.jsp", request, response);
    }
}
*/
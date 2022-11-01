package taller2.servlets.Funcion;

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
import java.util.Map;

@WebServlet(name = "ListadoFunciones", value = "/listado-funciones")
public class ListadoFuncionesServlet extends HttpServlet {
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
        String filtroEspectaculo = request.getParameter("filtroEspectaculo") != null ? request.getParameter("filtroEspectaculo") : "";
        Map<String, Plataforma> plataformas;
        Map<String, Espectaculo> espectaculos;
        Map<String, Funcion> funcionesFiltradas;
    
        // Cargar opciones plataformas y categorias para el filtrado
        plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);
    
        espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
        request.setAttribute("espectaculos", espectaculos);
        
        // Si se llega con un filtrado vacio
        if(filtroPlataforma.isEmpty() && filtroEspectaculo.isEmpty()) {
            funcionesFiltradas = fabrica.getIFuncion().obtenerFunciones();
            request.setAttribute("funcionesFiltradas", funcionesFiltradas);
        }
        // Si se llega con un filtrado de plataforma
        else if (!filtroPlataforma.isEmpty() && filtroEspectaculo.isEmpty()) {
            funcionesFiltradas = fabrica.getIFuncion().obtenerFuncionesDePlataforma(filtroPlataforma);
            request.setAttribute("funcionesFiltradas", funcionesFiltradas);
        }
        // Si llega con un filtrado espectaculo tambien llegara con uno de plataforma
        else {
            funcionesFiltradas = fabrica.getIFuncion().obtenerFuncionesDeEspectaculo(filtroPlataforma, filtroEspectaculo);
            request.setAttribute("funcionesFiltradas", funcionesFiltradas);
        }
        
        dispatchPage("/pages/funcion/listado-funciones.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

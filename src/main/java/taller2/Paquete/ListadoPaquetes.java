package taller2.Paquete;

import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;
import main.java.taller1.Logica.Clases.Paquete;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(name = "ListadoPaquetes", value = "/listado-paquetes")
public class ListadoPaquetes extends HttpServlet {
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
        Map<String, Paquete> paquetes = fabrica.getIPaquete().obtenerPaquetes();
        request.setAttribute("paquetes", paquetes);
        dispatchPage("/pages/paquete/listado-paquetes.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}

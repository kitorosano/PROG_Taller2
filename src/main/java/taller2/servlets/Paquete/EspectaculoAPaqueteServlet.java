package taller2.servlets.Paquete;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.EspectadorPaquete;
import main.java.taller1.Logica.Clases.Paquete;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EspectaculoAPaquete", value = "/espectaculoAPaquete")
public class EspectaculoAPaqueteServlet extends HttpServlet {

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
        boolean esArtista= (boolean) request.getSession().getAttribute("esArtista");
        if(esArtista) {
            String paquete = request.getParameter("paquete");
            Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
            //Obtengo los espectaculos que no estan en el paquete
            Map<String, Espectaculo> espectaculos= obtenerEspectaculosSinPaquete(paquete);
            request.setAttribute("plataformas", plataformas);
            request.setAttribute("espectaculos", espectaculos);
            request.setAttribute("paquete", paquete);
            dispatchPage("/pages/paquete/espectaculo-a-paquete.jsp", request, response);
        } else{
            System.out.println("No puede acceder a esta pagina");
            request.setAttribute("error", "No puede acceder a esta pagina");
            dispatchPage("/pages/index.jsp", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] espectaculosAagregar = request.getParameterValues("espAgregar");
        String nombrepaquete=request.getParameter("paquete");
        Map<String, Plataforma> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
        //Obtengo los espectaculos que no estan en el paquete
        Map<String, Espectaculo> espectaculosPaq= obtenerEspectaculosSinPaquete(nombrepaquete);
        request.setAttribute("plataformas", plataformas);
        request.setAttribute("espectaculos", espectaculosPaq);


        if(espectaculosAagregar!=null){
            Map<String,Espectaculo> espectaculos=fabrica.getIPaquete().obtenerEspectaculosDePaquete(nombrepaquete);
            for(String nuevo:espectaculosAagregar){
                if(espectaculos.get(nuevo)==null){
                    Espectaculo esp=fabrica.getIEspectaculo().obtenerEspectaculos().get(nuevo);
                    try {
                        fabrica.getIPaquete().altaEspectaculoAPaquete(esp.getNombre(),nombrepaquete,esp.getPlataforma().getNombre());
                    } catch (Exception e) {
                        System.out.println(e);
                        request.setAttribute("error", "Error al agregar los paquetes");
                        dispatchPage("/pages/paquete/espectaculo-a-paquete.jsp", request, response);
                    }
                }
            }
        }
        response.sendRedirect(request.getContextPath());
    }
    private Map<String, Espectaculo> obtenerEspectaculosSinPaquete(String paquete ){
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
        Map<String, Espectaculo> espectaculosPaquete = fabrica.getIPaquete().obtenerEspectaculosDePaquete(paquete);
        for (Espectaculo e : espectaculosPaquete.values()) {
            String clave = e.getNombre() + "-" + e.getPlataforma().getNombre();
            if (espectaculos.containsKey(clave)) {
                espectaculos.remove(clave, espectaculos.get(clave));
            }
        }
        return espectaculos;
    }
}

package taller2.servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
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
        String paquete=request.getParameter("paquete");
        Map<String, Plataforma> plataformas=fabrica.getIPlataforma().obtenerPlataformas();
        Map<String, Espectaculo> espectaculos=fabrica.getIEspectaculo().obtenerEspectaculos();
        Map<String, Espectaculo> espectaculosPaquete=fabrica.getIPaquete().obtenerEspectaculosDePaquete(paquete);
        for(Espectaculo e:espectaculosPaquete.values()){
            String clave=e.getNombre()+"-"+e.getPlataforma().getNombre();
            if(espectaculos.containsKey(clave)){
                espectaculos.remove(clave,espectaculos.get(clave));
            }
        }
        request.setAttribute("plataformas",plataformas);
        request.setAttribute("espectaculos",espectaculos);
        request.setAttribute("paquete",paquete);
        dispatchPage("/pages/espectaculo-a-paquete.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] espectaculosAagregar = request.getParameterValues("espAgregar");
        String nombrepaquete=request.getParameter("paquete");
        if(espectaculosAagregar!=null){
            Map<String,Espectaculo> espectaculos=fabrica.getIPaquete().obtenerEspectaculosDePaquete(nombrepaquete);
            for(String invitado:espectaculosAagregar){
                if(espectaculos.get(invitado)==null){
                    Espectaculo esp=fabrica.getIEspectaculo().obtenerEspectaculos().get(invitado);
                    try {
                        fabrica.getIPaquete().altaEspectaculoAPaquete(esp.getNombre(),nombrepaquete,esp.getPlataforma().getNombre());
                    } catch (Exception e) {
                        System.out.println(e);
                        request.setAttribute("error", "Error al agregar los paquetes");
                        dispatchPage("/pages/espectaculo-a-paquete.jsp", request, response);
                    }
                }
            }
        }
        response.sendRedirect(request.getContextPath());
    }
}

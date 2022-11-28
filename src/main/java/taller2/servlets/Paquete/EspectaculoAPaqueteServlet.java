package taller2.servlets.Paquete;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.CategoriaDTO;
import main.java.taller1.Logica.DTOs.PaqueteDTO;
import main.java.taller1.Logica.DTOs.PlataformaDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "EspectaculoAPaquete", value = "/registro-espectaculo-a-paquete")
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
    
    protected boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Si no hay sesión, redirigir a login
        if (session == null) {
            return false;
        }
        
        // Si hay sesión, obtener el usuario
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        
        // Si no hay usuario, redirigir a login
        if (usuarioLogueado == null) {
            return false;
        }
        
        // Si hay usuario, enviarlo a la página de inicio
        return true;
    }
    
    protected void dispatchError(String errorMessage, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("message", errorMessage);
        request.setAttribute("messageType","error");
        RequestDispatcher view = request.getRequestDispatcher("/pages/espectaculo/registro-espectaculo.jsp");
        view.forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Si no hay sesión, redirigir a login
        boolean sessionIniciada = checkSession(request, response);
        try {
            if(sessionIniciada) {
                Map<String, PlataformaDTO> todasPlataformas = fabrica.getIPlataforma().obtenerPlataformas();
                Map<String, Espectaculo> todosEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
                Map<String, PaqueteDTO> todosPaquetes = fabrica.getIPaquete().obtenerPaquetes();
                Map<String, CategoriaDTO> todasCategorias = fabrica.getICategoria().obtenerCategorias();
                Map<String, Usuario> todosUsuarios = fabrica.getIUsuario().obtenerUsuarios();
            
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
            
                HttpSession session = request.getSession();
                boolean esArtista= (boolean) session.getAttribute("esArtista");
                if(esArtista) {
                    String paquete = request.getParameter("paquete");
                    Map<String, PlataformaDTO> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
                    //Obtengo los espectaculos que no estan en el paquete
                    Map<String, Espectaculo> espectaculos= obtenerEspectaculosSinPaquete(paquete);
                    request.setAttribute("plataformas", plataformas);
                    request.setAttribute("espectaculos", espectaculos);
                    request.setAttribute("paquete", paquete);
                    dispatchPage("/pages/paquete/registro-espectaculo-a-paquete.jsp", request, response);
                }else{
                    dispatchPage("/pages/404.jsp", request, response);
                }
            } else {
                response.sendRedirect("login");
            }
        } catch (RuntimeException e) {
            dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] espectaculosAagregar = request.getParameterValues("espAgregar");
        String nombrepaquete=request.getParameter("paquete");
        Map<String, PlataformaDTO> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
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
                        dispatchError("Error al agregar los paquetes", request, response);
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

package taller2.servlets.Paquete;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import taller2.DTOs.*;
import taller2.utils.FetchApiOptions;
import taller2.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "EspectaculoAPaquete", value = "/registro-espectaculo-a-paquete")
public class EspectaculoAPaqueteServlet extends HttpServlet {
    


    
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
        UsuarioDTO usuarioLogueado = (UsuarioDTO) session.getAttribute("usuarioLogueado");
        
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
                Map<String, PlataformaDTO> todasPlataformas = (Map<String, PlataformaDTO>) Utils.FetchApi("/plataformas").getEntity();
                Map<String, EspectaculoDTO> todosEspectaculos = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos").getEntity();
                Map<String, PaqueteDTO> todosPaquetes = (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes").getEntity();
                Map<String, CategoriaDTO> todasCategorias  = (Map<String, CategoriaDTO>) Utils.FetchApi("/categorias").getEntity();
                Map<String, UsuarioDTO> todosUsuarios = (Map<String, UsuarioDTO>) Utils.FetchApi("/usuarios").getEntity();
            
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
            
                HttpSession session = request.getSession();
                boolean esArtista= (boolean) session.getAttribute("esArtista");
                if(esArtista) {
                    String paquete = request.getParameter("paquete");
                    Map<String, PlataformaDTO> plataformas = (Map<String, PlataformaDTO>) Utils.FetchApi("/plataformas").getEntity();
                    //Obtengo los espectaculos que no estan en el paquete
                    Map<String, EspectaculoDTO> espectaculos= obtenerEspectaculosSinPaquete(paquete);
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
        Map<String, PlataformaDTO> plataformas = (Map<String, PlataformaDTO>) Utils.FetchApi("/plataformas").getEntity();
        //Obtengo los espectaculos que no estan en el paquete
        Map<String, EspectaculoDTO> espectaculosPaq= obtenerEspectaculosSinPaquete(nombrepaquete);
        request.setAttribute("plataformas", plataformas);
        request.setAttribute("espectaculos", espectaculosPaq);


        if(espectaculosAagregar!=null){
            Map<String,EspectaculoDTO> espectaculos= (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos/findByPaquete?paquete="+nombrepaquete).getEntity();
            for(String nuevo:espectaculosAagregar){
                EspectaculoDTO espectaculodto = espectaculos.get(nuevo);
                if(espectaculodto == null){ // si no existe el espectaculo en el paquete
                    try {
                        AltaEspectaculoAPaqueteDTO altaEspectaculoAPaqueteDTO = new AltaEspectaculoAPaqueteDTO();
                        altaEspectaculoAPaqueteDTO.setNombrePaquete(nombrepaquete);
                        altaEspectaculoAPaqueteDTO.setNombreEspectaculo(nuevo);
                        altaEspectaculoAPaqueteDTO.setNombrePlataforma(espectaculosPaq.get(nuevo).getPlataforma().getNombre());
                        
                        FetchApiOptions options = new FetchApiOptions();
                        options.setMethod("POST");
                        options.setBody(altaEspectaculoAPaqueteDTO);
                        
                        Utils.FetchApi("/paquetes/altaEspectaculoAPaquete", options);
                    } catch (Exception e) {
                        System.out.println(e);
                        dispatchError("Error al agregar los paquetes", request, response);
                    }
                }
            }
        }
        response.sendRedirect(request.getContextPath());
    }
    private Map<String, EspectaculoDTO> obtenerEspectaculosSinPaquete(String paquete ){
        Map<String, EspectaculoDTO> espectaculos = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos").getEntity();
        Map<String, EspectaculoDTO> espectaculosPaquete = (Map<String, EspectaculoDTO>) Utils.FetchApi("/espectaculos/findByPaquete?nombrePaquete="+paquete).getEntity();
        for (EspectaculoDTO e : espectaculosPaquete.values()) {
            String clave = e.getNombre() + "-" + e.getPlataforma().getNombre();
            if (espectaculos.containsKey(clave)) {
                espectaculos.remove(clave, espectaculos.get(clave));
            }
        }
        return espectaculos;
    }
}

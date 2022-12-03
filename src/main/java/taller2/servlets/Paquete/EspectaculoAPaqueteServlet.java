package taller2.servlets.Paquete;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.IOException;
import java.util.Map;


@WebServlet(name = "EspectaculoAPaquete", value = "/registro-espectaculo-a-paquete")
public class EspectaculoAPaqueteServlet extends HttpServlet {
    
    Fetch fetch;
    
    public void init() {
        fetch = new Fetch();
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
                
                Map<String, PlataformaDTO> todasPlataformas = fetch.Set("/plataformas/findAll").Get().getMapPlataforma();
                Map<String, EspectaculoDTO> todosEspectaculos = fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
                Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll").Get().getMapPaquete();
                Map<String, CategoriaDTO> todasCategorias = fetch.Set("/categorias/findAll").Get().getMapCategoria();
                Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll").Get().getMapUsuario();
            
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
            
                HttpSession session = request.getSession();
                boolean esArtista= (boolean) session.getAttribute("esArtista");
                if(esArtista) {
                    String paquete = request.getParameter("paquete");
                    Map<String, PlataformaDTO> plataformas = fetch.Set("/plataformas").Get().getMapPlataforma();
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
        Map<String, PlataformaDTO> plataformas = fetch.Set("/plataformas").Get().getMapPlataforma();
        //Obtengo los espectaculos que no estan en el paquete
        Map<String, EspectaculoDTO> espectaculosPaq= obtenerEspectaculosSinPaquete(nombrepaquete);
        request.setAttribute("plataformas", plataformas);
        request.setAttribute("espectaculos", espectaculosPaq);


        if(espectaculosAagregar!=null){
            Map<String, EspectaculoDTO> espectaculos= fetch.Set("/espectaculos/findByPaquete?paquete="+nombrepaquete).Get().getMapEspectaculo();
            for(String nuevo:espectaculosAagregar){
                EspectaculoDTO espectaculodto = espectaculos.get(nuevo);
                if(espectaculodto == null){ // si no existe el espectaculo en el paquete
                    try {
                        AltaEspectaculoAPaqueteDTO altaEspectaculoAPaqueteDTO = new AltaEspectaculoAPaqueteDTO();
                        altaEspectaculoAPaqueteDTO.setNombrePaquete(nombrepaquete);
                        altaEspectaculoAPaqueteDTO.setNombreEspectaculo(nuevo);
                        altaEspectaculoAPaqueteDTO.setNombrePlataforma(espectaculosPaq.get(nuevo).getPlataforma().getNombre());
                        
                        fetch.Set("/paquetes/altaEspectaculoAPaquete", altaEspectaculoAPaqueteDTO).Post();
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
        try {
            Map<String, EspectaculoDTO> espectaculos = fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
            Map<String, EspectaculoDTO> espectaculosPaquete = fetch.Set("/espectaculos/findByPaquete?nombrePaquete="+paquete).Get().getMapEspectaculo();
            for (EspectaculoDTO e : espectaculosPaquete.values()) {
                String clave = e.getNombre() + "-" + e.getPlataforma().getNombre();
                if (espectaculos.containsKey(clave)) {
                    espectaculos.remove(clave, espectaculos.get(clave));
                }
            }
            return espectaculos;
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package taller2.servlets.Espectaculo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ListadoEspectaculos", value = "/listado-espectaculos")
public class ListadoEspectaculosServlet extends HttpServlet {
  
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
        RequestDispatcher view = request.getRequestDispatcher("/pages/index.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // Si no hay sesión, redirigir a login
      boolean logueado = checkSession(request, response);
      try {
            if(!logueado) {
              response.sendRedirect("login");
              return;
            }
            Map<String, PlataformaDTO> todasPlataformas =  fetch.Set("/plataformas/findAll").Get().getMapPlataforma();
            Map<String, EspectaculoDTO> todosEspectaculos =  fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
            Map<String, PaqueteDTO> todosPaquetes = fetch.Set("/paquetes/findAll/").Get().getMapPaquete();
            Map<String, CategoriaDTO> todasCategorias = fetch.Set("/categorias/findAll/").Get().getMapCategoria();
            Map<String, UsuarioDTO> todosUsuarios = fetch.Set("/usuarios/findAll/").Get().getMapUsuario();
        
            request.setAttribute("todasPlataformas", todasPlataformas);
            request.setAttribute("todosEspectaculos", todosEspectaculos);
            request.setAttribute("todosPaquetes", todosPaquetes);
            request.setAttribute("todasCategorias", todasCategorias);
            request.setAttribute("todosUsuarios", todosUsuarios);
            String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
            String filtroCategoria = request.getParameter("filtroCategoria") != null ? request.getParameter("filtroCategoria") : "";
            Map<String, EspectaculoDTO> espectaculosFiltrados = new HashMap<>();
            Map<String, Map<String, CategoriaDTO>> categoriasEspectaculosFiltrados = new HashMap<>();

            //Obtengo los espectaculos favoritos del usuario
            HttpSession session = request.getSession();
            String usuarioLogueadoNickname = ((UsuarioDTO) session.getAttribute("usuarioLogueado")).getNickname();

            Map<String, String> espectaculosFavoritos = fetch.Set("/usuarios/findEspectaculosFavoritos?nickname="+usuarioLogueadoNickname).Get().getMapEspectaculosFavoritos();
            request.setAttribute("espectaculosFavoritos", espectaculosFavoritos);

            // Si se llega con un filtrado vacio
            if(filtroPlataforma.isEmpty() && filtroCategoria.isEmpty()) {
                espectaculosFiltrados = fetch.Set("/espectaculos/findAll").Get().getMapEspectaculo();
                request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
            }
            // Si se llega con un filtrado de plataforma
            else if (!filtroPlataforma.isEmpty() && filtroCategoria.isEmpty()) {
                //espectaculosFiltrados = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(filtroPlataforma);

                espectaculosFiltrados = fetch.Set("/espectaculos/findByPlataforma?nombrePlataforma="+filtroPlataforma).Get().getMapEspectaculo();
                request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
            }
            // Si se llega con un filtrado de categoria
            else if (filtroPlataforma.isEmpty() && !filtroCategoria.isEmpty()) {
                //espectaculosFiltrados = fabrica.getICategoria().obtenerEspectaculosDeCategoria(filtroCategoria);

                espectaculosFiltrados = fetch.Set("/espectaculos/findByCategoria?nombreCategoria="+filtroCategoria).Get().getMapEspectaculo();
                request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
            }
            // Si llega con un filtrado de plataforma y categoria
            else {
              
                //Map<String, EspectaculoDTO> espectaculosDePlataforma = fabrica.getIEspectaculo().obtenerEspectaculosPorPlataforma(filtroPlataforma);
                Map<String, EspectaculoDTO> espectaculosDePlataforma = fetch.Set("/espectaculos/findByPlataforma?nombrePlataforma="+filtroPlataforma).Get().getMapEspectaculo();

                //Map<String, EspectaculoDTO> espectaculosDeCategoria = fabrica.getICategoria().obtenerEspectaculosDeCategoria(filtroCategoria);
                Map<String, EspectaculoDTO> espectaculosDeCategoria = fetch.Set("/espectaculos/findByCategoria?nombreCategoria="+filtroCategoria).Get().getMapEspectaculo();

                for (EspectaculoDTO espectaculo : espectaculosDeCategoria.values()){
                    if (espectaculo.getPlataforma().getNombre().equals(filtroPlataforma)){
                        espectaculosFiltrados.put(espectaculo.getNombre(), espectaculo);
                    }
                }
                request.setAttribute("espectaculosFiltrados", espectaculosFiltrados);
            }
            
            // Cargar categorias de los espectaculos filtrados
            for (EspectaculoDTO espectaculo : espectaculosFiltrados.values()){
                //Map<String,CategoriaDTO> categoriasEspectaculoFiltrado = fabrica.getICategoria().obtenerCategoriasDeEspectaculo(espectaculo.getNombre());
                Map<String,CategoriaDTO> categoriasEspectaculoFiltrado = fetch.Set("/categorias/findByEspectaculoAndPlataforma?nombreEspectaculo="+espectaculo.getNombre() + "&nombrePlataforma="+ espectaculo.getPlataforma().getNombre()).Get().getMapCategoria();
                categoriasEspectaculosFiltrados.put(espectaculo.getNombre(), categoriasEspectaculoFiltrado);
            }
            request.setAttribute("categoriasEspectaculosFiltrados", categoriasEspectaculosFiltrados);
            
            dispatchPage("/pages/espectaculo/listado-espectaculos.jsp", request, response);
        } catch (RuntimeException e) {
            dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean tipoAccion = Boolean.parseBoolean(request.getParameter("accion"));

        EspectaculoFavoritoDTO dto = new EspectaculoFavoritoDTO();

        String nickname = request.getParameter("nickname");
        String nombreEspectaculo = request.getParameter("nombreEspectaculo");
        String nombrePlataforma = request.getParameter("nombrePlataforma");

        System.out.println(nickname);
        System.out.println(nombreEspectaculo);
        System.out.println(nombrePlataforma);

        dto.setNickname(nickname);
        dto.setNombreEspectaculo(nombreEspectaculo);
        dto.setNombrePlataforma(nombrePlataforma);

        if (tipoAccion == true) {
            fetch.Set("/usuarios/createEspectaculoFavorito", dto).Post();
        } else {
            fetch.Set("/usuarios/deleteEspectaculoFavorito", dto).Delete();
        }
    }
}

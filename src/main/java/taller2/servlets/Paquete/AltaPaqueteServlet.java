package taller2.servlets.Paquete;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import taller2.DTOs.*;
import taller2.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@WebServlet(name = "AltaPaquete", value = "/registro-paquete")
@MultipartConfig
public class AltaPaqueteServlet extends HttpServlet {



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
                    dispatchPage("/pages/paquete/registro-paquete.jsp", request, response);
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
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String vigencia = request.getParameter("vigencia");
        String descuento = request.getParameter("descuento");
        Part part=request.getPart("imagen");

        if(camposVacios(nombre,descripcion,vigencia,descuento)){
            dispatchError("Los campos obligatorios no pueden ser vacios", request, response);
            return;
        }
        double descuentoDb= Double.parseDouble(descuento);
        LocalDate vigenciaDate=LocalDate.parse(vigencia);
        if(nombreExistente(nombre)){
            dispatchError("El nombre ingresado ya existe", request, response);
            return;
        }

        String urlImagen="https://i.imgur.com/hHn0WrG.png";
        try {
            if(part.getSize()!=0){
                InputStream inputImagen=part.getInputStream();
                urlImagen=fabrica.getIDatabase().guardarImagen((FileInputStream) inputImagen);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            dispatchError("Error al guardar la imagen", request, response);
            return;
        }
        PaqueteDTO nuevo = new PaqueteDTO(nombre,descripcion,descuentoDb, LocalDateTime.of(vigenciaDate, LocalTime.parse("00:00:00")), LocalDateTime.now(), urlImagen);

        try {
            fabrica.getIPaquete().altaPaquete(nuevo);
            response.sendRedirect(request.getContextPath()); // redirigir a un servlet (por url)
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            dispatchError("Error al crear el paquete", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }

    private boolean camposVacios(String nombre, String descripcion, String vigencia, String descuento) {
        return nombre == null || descripcion == null || vigencia == null || descuento == null ||
                nombre.isEmpty() || descripcion.isEmpty() || vigencia.isEmpty() || descuento.isEmpty();
    }

    private boolean nombreExistente(String nombrepaq) {      //Devuelve true si hay error
        Map<String, PaqueteDTO> paquetes = (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes").getEntity();
        for (PaqueteDTO paq : paquetes.values()) {
            if (paq.getNombre().equals(nombrepaq)) {
                return true;
            }
        }
        return false;
    }
}

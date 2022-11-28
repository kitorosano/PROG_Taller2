package taller2.servlets.Espectaculo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.CategoriaDTO;
import main.java.taller1.Logica.DTOs.PaqueteDTO;
import main.java.taller1.Logica.DTOs.PlataformaDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "AltaEspectaculo", value = "/registro-espectaculo")
@MultipartConfig
public class AltaEspectaculoServlet extends HttpServlet {

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
                boolean esArtista = session.getAttribute("esArtista") != null && (boolean) session.getAttribute("esArtista");
                
                // Si no es artista, redirigir a la página 404
                if(esArtista){
                    dispatchPage("/pages/espectaculo/registro-espectaculo.jsp", request, response);
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
        HttpSession session = request.getSession();
        Artista artistaLogueado = (Artista) session.getAttribute("usuarioLogueado");
        
        String nombre = request.getParameter("nombre");
        String nombplataforma = request.getParameter("plataforma");
        String descripcion = request.getParameter("descripcion");
        String duracionstr = request.getParameter("duracion");
        String espMaximosstr = request.getParameter("espMaximos");
        String espMinimosstr = request.getParameter("espMinimos");
        String url = request.getParameter("url");
        String costostr = request.getParameter("costo");
        Part part=request.getPart("imagen");
        String[] str_categoriasElegidas = request.getParameterValues("catElegidas");
        List<String> categoriasElegidas = new ArrayList<>(
                Arrays.asList(str_categoriasElegidas)
        );
        categoriasElegidas.forEach(System.out::println);
        
        // Seteo valores para los campos select del formulario
        try {
            Map<String, PlataformaDTO> plataformas = fabrica.getIPlataforma().obtenerPlataformas();
            request.setAttribute("plataformas", plataformas);
            Map<String, CategoriaDTO> categorias = fabrica.getICategoria().obtenerCategorias();
            request.setAttribute("categorias", categorias);
        } catch (RuntimeException e) {
            dispatchError("Error al obtener las plataformas y categorias", request, response);
            return;
        }
        
        // Validaciones
        if(artistaLogueado==null){
            dispatchError("Usuario no valido para agregar espectaculo", request, response);
            return;
        }
        if(camposVacios(nombre,nombplataforma,descripcion,duracionstr,espMaximosstr,espMinimosstr,url,costostr)){
            dispatchError("Los campos obligatorios no pueden ser vacios", request, response);
            return;
        }
        
        // Convertir a numeros
        double duracion= Double.parseDouble(duracionstr);
        int espMaximos= Integer.parseInt(espMaximosstr);
        int espMinimos= Integer.parseInt(espMinimosstr);
        double costo= Double.parseDouble(costostr);
        
        // + Validaciones
        try {
            if (nombreExistente(nombre, nombplataforma)) {
                dispatchError("El nombre ingresado ya existe", request, response);
                return;
            }
        } catch (RuntimeException e){
            dispatchError("Error al validar nombre existente", request, response);
            return;
        }
        
        if(cantidadEspectadores(espMaximos,espMinimos)){
            dispatchError("Error con la cantidad de espectadores", request, response);
            return;
        }
        if (!esFormatoUrl(url)){
            dispatchError("Formato de url invalida", request, response);
            return;
        }

        String urlImagen="https://i.imgur.com/BeJ3HuS.png";
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
    
        // Obtener plataforma
        PlataformaDTO plataforma;
        try {
            Optional<PlataformaDTO> optional = fabrica.getIPlataforma().obtenerPlataforma(nombplataforma);
            if (!optional.isPresent()) {
                dispatchError("Error, plataforma no encontrada", request, response);
                return;
            }
            plataforma = optional.get();
        } catch (RuntimeException e) {
            e.printStackTrace();
            dispatchError("Error al crear el espectaculo", request, response);
            return;
        }
        
        Espectaculo nuevoEspectaculo = new Espectaculo(nombre, descripcion, duracion, espMinimos, espMaximos, url, costo, E_EstadoEspectaculo.INGRESADO, LocalDateTime.now(), urlImagen, plataforma, artistaLogueado);
        try {
            fabrica.getIEspectaculo().altaEspectaculo(nuevoEspectaculo);
            for(String categoria : categoriasElegidas){
                fabrica.getICategoria().altaCategoriaAEspectaculo(categoria, nuevoEspectaculo.getNombre(), nuevoEspectaculo.getPlataforma().getNombre());
            }
            
            // No hacer un redirect, mas bien un dispatch a la pagina, para mostrar el mensaje de exito
            request.setAttribute("message", "Espectaculo creado con exito");
            request.setAttribute("messageType", "success");
            dispatchPage("/pages/espectaculo/registro-espectaculo.jsp", request, response);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            dispatchError("Error al crear el espectaculo", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }

    // METODOS AUXILIARES PARA VALIDACIONES
    private boolean camposVacios(String nombre, String nombplataforma, String descripcion, String duracion, String espMaximos, String espMinimos, String url, String costo) {
        return nombre == null || nombplataforma == null || descripcion == null || espMinimos == null || espMaximos == null || url == null || costo == null ||
                nombre.isEmpty() || nombplataforma.isEmpty() || descripcion.isEmpty() || duracion.isEmpty() || espMaximos.isEmpty() || espMinimos.isEmpty() || url.isEmpty() || costo.isEmpty();
    }

    private boolean nombreExistente(String nombreesp, String plataforma) {      //Devuelve true si hay error
        Optional<Espectaculo> espectaculo = fabrica.getIEspectaculo().obtenerEspectaculo(plataforma, nombreesp);
        return espectaculo.isPresent();
    }

    private boolean cantidadEspectadores(int maximo, int minimo) {      //Devuelve true si hay error
        if (minimo < 0)
            return true;
        return minimo >= maximo;
    }

    private boolean esFormatoUrl(String url){
        String regexURL = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
        if(!url.matches(regexURL)){
            return false;
        }
        return true;
    }

}

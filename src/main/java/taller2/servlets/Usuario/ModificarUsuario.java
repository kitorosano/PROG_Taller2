package taller2.servlets.Usuario;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

@WebServlet(name = "ModificarUsuario", value = "/modificar-usuario")
@MultipartConfig
public class ModificarUsuario extends HttpServlet {
    
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
        RequestDispatcher view = request.getRequestDispatcher("/pages/usuario/modificar-usuario.jsp");
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
                
                dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
    
            } else {
                response.sendRedirect("login");
            }
        } catch (RuntimeException e) {
            dispatchError("Error al obtener datos para los componentes de la pagina", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDTO usuarioSession= (UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");
        String nickname= usuarioSession.getNickname();
        UsuarioDTO usu= fetch.Set("/usuarios/findByNickname?nickname="+nickname).Get().getUsuario();
        if(usu!=null) {
            //usu = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
            request.setAttribute("tipo", "espectador");
            if(usu.isEsArtista()){
                request.setAttribute("tipo", "artista");
            }
        }else{
            dispatchError("Nickname no valido", request, response);
            return;
        }

        String tipo;
        if ((boolean) request.getSession().getAttribute("esArtista") == false){
            tipo = "espectador";
        }else{
            tipo = "artista";
        }

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String contrasenia = request.getParameter("contrasenia");
        String fechaNac_str = request.getParameter("fechaNac");
        Part part=request.getPart("imagen");
        String descripcion = request.getParameter("descripcion");
        String biografia = request.getParameter("biografia");
        String url = request.getParameter("url");


        // Validar los datos traidos del formulario:

        //error cuando alguno de los campos son vacios
        if(camposVacios(nombre, apellido, fechaNac_str, contrasenia)) {
            dispatchError("Los campos obligatorios no pueden ser vacios", request, response);
            return;
        }

        LocalDate fechaNac = LocalDate.parse(fechaNac_str); // ahora que sabemos que no es vacio, lo podemos parsear

        //La fecha no es valida porque no nacio mañana
        if(!fechaValida(fechaNac)){
            dispatchError("La fecha no es valida", request, response);
            return;
        }

        String urlImagen="";
        if(part.getSize()!=0){
            InputStream inputImagen=part.getInputStream();
            urlImagen= fetch.PostImage((FileInputStream) inputImagen).getContent();
            //urlImagen= Fabrica.getInstance().getIDatabase().guardarImagen((FileInputStream) inputImagen);
        }
        if(usu!=null){
            usu.setNombre(nombre);
            usu.setApellido(apellido);
            usu.setFechaNacimiento(fechaNac.toString());
            usu.setContrasenia(contrasenia);
            if(!urlImagen.equals(""))
                usu.setImagen(urlImagen);
        }

        // Se especifica el tipo de usuario a crear
        if(tipo.equals("artista")){
            if(camposVaciosArtista(descripcion)){
                dispatchError("Los campos obligatorios no pueden ser vacios", request, response);
                return;
            }
            if(!esFormatoUrl(url)){
                dispatchError("El url no es valido", request, response);
                return;
            }

            usu.setDescripcion(descripcion);
            usu.setBiografia(biografia);
            usu.setSitioWeb(url);
        }
        try {
            // Se modifica el usuario en la base de datos
            fetch.Set("/usuarios/updateByNickname",usu).Put();
            usuarioSession.setNombre(usu.getNombre());
            usuarioSession.setApellido(usu.getApellido());
            usuarioSession.setContrasenia(usu.getContrasenia());
            usuarioSession.setFechaNacimiento(usu.getFechaNacimiento());
            usuarioSession.setImagen(usu.getImagen());

            if(tipo.equals("artista")){
                usuarioSession.setDescripcion(usu.getDescripcion());
                usuarioSession.setBiografia(usu.getBiografia());
                usuarioSession.setSitioWeb(usu.getSitioWeb());
            }

            //fabrica.getIUsuario().modificarUsuario(usu);

            // Redireccionar a la pantalla de login
            request.getSession().setAttribute("message", "Informacion modificada exitosamente");
            response.sendRedirect("perfil"); // redirijir a un servlet (por url)
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            // Error al crear el usuario
            dispatchError("Error al modificar informacion", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
            return;
        }
    }

    private boolean camposVacios(String nombre, String apellido, String fechaNac, String contrasenia){
        return nombre==null || apellido==null || fechaNac==null || contrasenia==null ||
                nombre.isEmpty() || apellido.isEmpty() || fechaNac.isEmpty() ||  contrasenia.isEmpty();
    }
    private boolean camposVaciosArtista(String descripcion){
        return descripcion==null || descripcion.isEmpty();
    }
    private boolean fechaValida(LocalDate fecha){
        LocalDate hoy = LocalDate.now();
        return fecha.isEqual(hoy) || fecha.isBefore(hoy);
    }
    private boolean esFormatoUrl(String url){
        String regexURL = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
        if(!url.matches(regexURL)){
            return false;
        }
        return true;
    }
}

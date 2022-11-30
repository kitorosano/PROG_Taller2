package taller2.servlets.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.CategoriaDTO;
import main.java.taller1.Logica.DTOs.EspectaculoDTO;
import main.java.taller1.Logica.DTOs.PaqueteDTO;
import main.java.taller1.Logica.DTOs.PlataformaDTO;
import main.java.taller1.Logica.DTOs.UsuarioDTO;
import main.java.taller1.Logica.Fabrica;
import taller2.DTOs.*;
import taller2.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

@WebServlet(name = "ModificarUsuario", value = "/modificar-usuario")
@MultipartConfig
public class ModificarUsuario extends HttpServlet {



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
        UsuarioDTO usu=null;
        if(fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent()) {
            usu = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
            request.setAttribute("tipo", "espectador");
            if(usu instanceof Artista){
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
            urlImagen= Fabrica.getInstance().getIDatabase().guardarImagen((FileInputStream) inputImagen);
        }
        if(usu!=null){
            usu.setNombre(nombre);
            usu.setApellido(apellido);
            usu.setFechaNacimiento(fechaNac);
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

            ((Artista) usu).setDescripcion(descripcion);
            ((Artista) usu).setBiografia(biografia);
            ((Artista) usu).setSitioWeb(url);
        }
        try {
            // Se crea el usuario en la base de datos
            fabrica.getIUsuario().modificarUsuario(usu);

            // Redireccionar a la pantalla de login
            request.getSession().setAttribute("message", "Informacion modificada exitosamente");
            response.sendRedirect("perfil"); // redirijir a un servlet (por url)
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            // Error al crear el usuario
            dispatchError("Error al crear el usuario", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
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
        return regexURL.matches(url);
    }
}

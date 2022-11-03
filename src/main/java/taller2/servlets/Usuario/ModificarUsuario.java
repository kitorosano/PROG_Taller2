package taller2.servlets.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectador;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@WebServlet(name = "ModificarUsuario", value = "/modificar-usuario")
@MultipartConfig
public class ModificarUsuario extends HttpServlet {

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
        dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuarioSession= (Usuario) request.getSession().getAttribute("usuarioLogueado");
        String nickname= usuarioSession.getNickname();
        Usuario usu=null;
        if(fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent()) {
            usu = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
            request.setAttribute("tipo", "espectador");
            if(usu instanceof Artista){
                request.setAttribute("tipo", "artista");
            }
        }else{
            request.setAttribute("error", "Nickname no valido");
            dispatchPage("/pages/index.jsp", request, response);
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
            request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
            return;
        }

        LocalDate fechaNac = LocalDate.parse(fechaNac_str); // ahora que sabemos que no es vacio, lo podemos parsear

        //La fecha no es valida porque no nacio mañana
        if(!fechaValida(fechaNac)){
            request.setAttribute("error", "La fecha no es valida");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
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
                request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
                dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
                return;
            }
            if(!esFormatoUrl(url)){
                request.setAttribute("error", "El url no es valido");
                dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
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
            request.setAttribute("error", "Error al crear el usuario");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
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

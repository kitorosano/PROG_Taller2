package taller2.Usuario;

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
        String nickname=request.getParameter("nickname");
        String nicknameSession= (String) request.getSession().getAttribute("nickname");
        Usuario usuario=null;
        if(fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent() && nickname.equals(nicknameSession)){
            usuario= fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
            request.setAttribute("nombre",usuario.getNombre());
            request.setAttribute("apellido",usuario.getApellido());
            request.setAttribute("contrasenia",usuario.getContrasenia());
            request.setAttribute("fechaNac",usuario.getFechaNacimiento().toString());
            request.setAttribute("tipo","espectador");
            if(usuario instanceof Artista){
                request.setAttribute("descripcion",((Artista) usuario).getDescripcion());
                request.setAttribute("biografia", ((Artista) usuario).getBiografia());
                request.setAttribute("url",((Artista) usuario).getSitioWeb());
                request.setAttribute("tipo","artista");
            }
            request.setAttribute("peticion","get");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
        }else{
            System.out.println("Nickname no valido");
            request.setAttribute("error", "Nickname no valido");
            dispatchPage("/pages/home.jsp", request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname= (String) request.getSession().getAttribute("nickname");
        Usuario usu=null;
        if(fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).isPresent()) {
            usu = fabrica.getIUsuario().obtenerUsuarioPorNickname(nickname).get();
            request.setAttribute("tipo", "espectador");
            if(usu instanceof Artista){
                request.setAttribute("tipo", "artista");
            }
        }else{
            request.setAttribute("error", "Nickname no valido");
            dispatchPage("/pages/home.jsp", request, response);
        }
        String nombre = request.getParameter("nombre");
        String hola= request.getParameter("hola");
        String hola2= request.getParameter("hola2");
        String apellido = request.getParameter("apellido");
        String contrasenia = request.getParameter("contrasenia");
        String fechaNac_str = request.getParameter("fechaNac");
        String contrasenia2 = request.getParameter("contrasenia2");
        Part part=request.getPart("imagen");
        String descripcion = request.getParameter("descripcion");
        String biografia = request.getParameter("biografia");
        String url = request.getParameter("url");
        String tipo = request.getParameter("tipo");
        request.setAttribute("peticion","post");



        // Validar los datos traidos del formulario:

        //error cuando alguno de los campos son vacios
        if(camposVacios(nombre, apellido, fechaNac_str, contrasenia, contrasenia2)) {
            request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
            return;
        }

        LocalDate fechaNac = LocalDate.parse(fechaNac_str); // ahora que sabemos que no es vacio, lo podemos parsear

        // Error contraseñas no machean
        if(!contraseniasIguales(contrasenia, contrasenia2)){
            request.setAttribute("error", "Las contraseñas no coinciden");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
            return;
        }
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
            /*if (esFormatoUrl(url)){
                request.setAttribute("error", "Formato de url invalida");
                dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response);
                return;
            }*/
            ((Artista) usu).setDescripcion(descripcion);
            ((Artista) usu).setBiografia(biografia);
            ((Artista) usu).setSitioWeb(url);
        }
        try {
            // Se crea el usuario en la base de datos
            fabrica.getIUsuario().modificarUsuario(usu);

            // Redireccionar a la pantalla de login
            request.getSession().setAttribute("message", "Informacion modificada exitosamente");
            response.sendRedirect("detalle-usuario"); // redirijir a un servlet (por url)
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            // Error al crear el usuario
            request.setAttribute("error", "Error al crear el usuario");
            dispatchPage("/pages/usuario/modificar-usuario.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }


    private boolean contraseniasIguales(String pass1, String pass2){
        return pass1.equals(pass2);
    }
    private boolean camposVacios(String nombre, String apellido, String fechaNac, String contrasenia, String contrasenia2){
        return nombre==null || apellido==null || fechaNac==null || contrasenia==null || contrasenia2==null ||
                nombre.isEmpty() || apellido.isEmpty() || fechaNac.isEmpty() ||  contrasenia.isEmpty() || contrasenia2.isEmpty();
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

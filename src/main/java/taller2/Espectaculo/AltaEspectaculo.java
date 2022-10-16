package taller2.Espectaculo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.Artista;
import main.java.taller1.Logica.Clases.Espectaculo;
import main.java.taller1.Logica.Clases.Plataforma;
import main.java.taller1.Logica.Clases.Usuario;
import main.java.taller1.Logica.Fabrica;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet(name = "AltaEspectaculo", value = "/alta-espectaculo")
public class AltaEspectaculo extends HttpServlet {

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
        Map<String, Plataforma> plataformas = fabrica.getIEspectaculo().obtenerPlataformas();
        request.setAttribute("plataformas", plataformas);
        dispatchPage("/pages/espectaculo/altaEspectaculo.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String nombplataforma = request.getParameter("plataforma");
        String descripcion = request.getParameter("descripcion");
        String duracionstr = request.getParameter("duracion");
        String  espMaximosstr = request.getParameter("espMaximos");
        String espMinimosstr = request.getParameter("espMinimos");
        String url = request.getParameter("url");
        String costostr = request.getParameter("costo");
        String imagen = request.getParameter("imagen");
        String[] categorias = request.getParameterValues("catElegidas");
        request.setAttribute("plataformas",fabrica.getIEspectaculo().obtenerPlataformas());
        //String nombArtista=(String)request.getSession().getAttribute("nickname");
        String nombArtista = "Kanlam";
        if(camposVacios(nombre,nombplataforma,descripcion,duracionstr,espMaximosstr,espMinimosstr,url,costostr,nombArtista)){
            request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
            dispatchPage("/pages/espectaculo/altaEspectaculo.jsp", request, response);
        }
        int duracion= Integer.parseInt(duracionstr);
        int espMaximos= Integer.parseInt(espMaximosstr);
        int espMinimos= Integer.parseInt(espMinimosstr);
        int costo= Integer.parseInt(costostr);
        if(nombreExistente(nombre,nombplataforma)){
            request.setAttribute("error", "El nombre ingresado ya existe");
            dispatchPage("/pages/espectaculo/altaEspectaculo.jsp", request, response);
        }
        if(cantidadEspectadores(espMaximos,espMinimos)){
            request.setAttribute("error", "Error con la cantidad de espectadores");
            dispatchPage("/pages/espectaculo/altaEspectaculo.jsp", request, response);
        }
        if (!esFormatoUrl(url)){
            request.setAttribute("error", "Formato de url invalida");
            dispatchPage("/pages/espectaculo/altaEspectaculo.jsp", request, response);
        }

        Map<String, Plataforma> plataformas = fabrica.getIEspectaculo().obtenerPlataformas();
        Plataforma p = plataformas.get(nombplataforma);
        Map<String, Usuario> usuarios = fabrica.getIUsuario().obtenerUsuarios();
        Artista art = (Artista) usuarios.get(nombArtista);
        Espectaculo nuevo = new Espectaculo(nombre, descripcion, duracion, espMinimos, espMaximos, url, costo, LocalDateTime.now(), p, art);

        try {
            fabrica.getIEspectaculo().altaEspectaculo(nuevo);
            response.sendRedirect("home"); // redirigir a un servlet (por url)
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            // Error al crear el usuario
            request.setAttribute("error", "Error al crear el usuario");
            dispatchPage("/pages/espectaculo/altaEspectaculo.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }

    private boolean camposVacios(String nombre, String nombplataforma, String descripcion, String duracion, String espMaximos, String espMinimos, String url, String costo, String nombArtista) {
        return nombre == null || nombplataforma == null || descripcion == null || nombArtista == null || espMinimos == null || espMaximos == null || url == null || costo == null ||
                nombre.isEmpty() || nombplataforma.isEmpty() || descripcion.isEmpty() || duracion.isEmpty() || espMaximos.isEmpty() || espMinimos.isEmpty() || url.isEmpty() || costo.isEmpty() || nombArtista.isEmpty();
    }

    private boolean nombreExistente(String nombreesp, String plataforma) {      //Devuelve true si hay error
        Map<String, Espectaculo> espectaculos = fabrica.getIEspectaculo().obtenerEspectaculos(plataforma);
        for (Espectaculo esp : espectaculos.values()) {
            if (esp.getNombre().equals(nombreesp)) {
                return true;
            }
        }
        return false;
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

package taller2.servlets.Paquete;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import main.java.taller1.Logica.Clases.Paquete;
import main.java.taller1.Logica.Fabrica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@WebServlet(name = "AltaPaquete", value = "/alta-paquete")
@MultipartConfig
public class AltaPaqueteServlet extends HttpServlet {
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
        dispatchPage("/pages/paquete/altaPaquete.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String vigencia = request.getParameter("vigencia");
        String descuento = request.getParameter("descuento");
        String urlImagen="";
        Part part=request.getPart("imagen");

        if(camposVacios(nombre,descripcion,vigencia,descuento)){
            request.setAttribute("error", "Los campos obligatorios no pueden ser vacios");
            dispatchPage("/pages/paquete/altaPaquete.jsp", request, response);
        }
        double descuentoDb= Double.parseDouble(descuento);
        LocalDate vigenciaDate=LocalDate.parse(vigencia);
        if(nombreExistente(nombre)){
            request.setAttribute("error", "El nombre ingresado ya existe");
            dispatchPage("/pages/paquete/altaPaquete.jsp", request, response);
        }

        if(part.getSize()!=0){
            InputStream inputImagen=part.getInputStream();
            urlImagen=fabrica.getIDatabase().guardarImagen((FileInputStream) inputImagen);
        }
        Paquete nuevo = new Paquete(nombre,descripcion,descuentoDb, LocalDateTime.of(vigenciaDate, LocalTime.parse("00:00:00")), LocalDateTime.now(), urlImagen);

        try {
            fabrica.getIPaquete().altaPaquete(nuevo);
            response.sendRedirect(request.getContextPath()); // redirigir a un servlet (por url)
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            // Error al crear el usuario
            request.setAttribute("error", "Error al crear el paquete");
            dispatchPage("/pages/paquete/altaPaquete.jsp", request, response); // devolver a una pagina (por jsp) manteniendo la misma url
        }
    }

    private boolean camposVacios(String nombre, String descripcion, String vigencia, String descuento) {
        return nombre == null || descripcion == null || vigencia == null || descuento == null ||
                nombre.isEmpty() || descripcion.isEmpty() || vigencia.isEmpty() || descuento.isEmpty();
    }

    private boolean nombreExistente(String nombrepaq) {      //Devuelve true si hay error
        Map<String, Paquete> paquetes = fabrica.getIPaquete().obtenerPaquetes();
        for (Paquete paq : paquetes.values()) {
            if (paq.getNombre().equals(nombrepaq)) {
                return true;
            }
        }
        return false;
    }
}

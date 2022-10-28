package taller2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "RegistroAFuncion", value = "/registroAFuncion")
public class RegistroAFuncion extends HttpServlet {

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
        String espectaculo=request.getParameter("nombre_espectaculo");
        String funcion=request.getParameter("nombre");
        String plataforma=request.getParameter("nombre_plataforma");
        Funcion fun = fabrica.getIFuncion().obtenerFuncion(plataforma,espectaculo,funcion).get();
        Map<String, EspectadorRegistradoAFuncion> registros = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador("Domainer");
        Map<String, Paquete> paquetes=obtenerPaquetesEspectadorEspectaculo(espectaculo,plataforma,"Domainer");

        request.setAttribute("funcion",fun);
        request.setAttribute("registros",registros);
        request.setAttribute("paquetes", paquetes);
        dispatchPage("/pages/registroEspectadores.jsp", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String espectaculo = request.getParameter("espectaculo");
        String funcion = request.getParameter("funcion");
        String plataforma = request.getParameter("plataforma");
        String paquete = request.getParameter("paquete");
        String espectador = "Domainer";
        double costo=0;
        String[] registrosCanjeados = request.getParameterValues("registrosCanjeados");

        Espectador esp= (Espectador) fabrica.getIUsuario().obtenerUsuarios().get(espectador);
        Funcion fun = (fabrica.getIFuncion().obtenerFuncion(plataforma, espectaculo, funcion).get());
        Map<String, EspectadorRegistradoAFuncion> registros = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador("Domainer");
        Map<String, Paquete> paquetes=obtenerPaquetesEspectadorEspectaculo(espectaculo,plataforma,"Domainer");
        Paquete paq=null;

        request.setAttribute("funcion",fun);
        request.setAttribute("registros",registros);
        request.setAttribute("paquetes", paquetes);

        Map<String, EspectadorRegistradoAFuncion> FuncionesCanjeadas = new HashMap<>();
        int cantMaxEspect = fabrica.getIFuncion().obtenerEspectadoresRegistradosAFuncion(funcion).size();

        if (cantMaxEspect == fun.getEspectaculo().getMaxEspectadores()) {
            request.setAttribute("error", "No se puede registrar, cantidad maxima alcanzada");
            dispatchPage("/pages/registroEspectadores.jsp", request, response);
        } else if (fabrica.getIFuncion().obtenerEspectadoresRegistradosAFuncion(funcion).get(espectador) != null) {
            request.setAttribute("error", "No se puede, ya esta registrado");
            dispatchPage("/pages/registroEspectadores.jsp", request, response);
        } else {
            if (registrosCanjeados != null) {
                if (registrosCanjeados.length == 3) {
                    for (String registro : registrosCanjeados) {
                        EspectadorRegistradoAFuncion canjeada = fabrica.getIFuncion().obtenerFuncionesRegistradasDelEspectador(espectador).get(registro);
                        canjeada.setCanjeado(true);
                        FuncionesCanjeadas.put(canjeada.getFuncion().getNombre(), canjeada);
                    }
                    costo = 0;
                } else{
                    request.setAttribute("error", "La cantidad de registros debe ser tres(3) para qeu el costo sea 0");
                    dispatchPage("/pages/registroEspectadores.jsp", request, response);
                }
            }else {
                costo = fun.getEspectaculo().getCosto();
            }
            if (!paquete.equals("undefined") && costo!=0) {
                paq = fabrica.getIPaquete().obtenerPaquetesPorEspectador(espectador).get(paquete).getPaquete();
                costo = fun.getEspectaculo().getCosto() - (fun.getEspectaculo().getCosto() * paq.getDescuento() / 100);
            }
            EspectadorRegistradoAFuncion nuevo= new EspectadorRegistradoAFuncion(esp,fun,paq,true,costo, LocalDateTime.now());
            try{
                fabrica.getIFuncion().registrarEspectadorAFuncion(nuevo);
                response.sendRedirect("home");
            } catch (Exception e) {
                System.out.println(e);
                request.setAttribute("error", "No se pudo registrar a la funcion");
                dispatchPage("/pages/registroEspectadores.jsp", request, response);
            }
        }
    }

    private Map<String,Paquete> obtenerPaquetesEspectadorEspectaculo(String espectaculo,String plataforma, String espectador ){
        Map<String, Paquete> paquetesEspectaculo = fabrica.getIPaquete().obtenerPaquetesDeEspectaculo(espectaculo);
        Map<String, EspectadorPaquete> paquetesEspectador = fabrica.getIPaquete().obtenerPaquetesPorEspectador(espectador);
        Map<String, Paquete> paquetes= new HashMap<>();
        for(EspectadorPaquete paq :paquetesEspectador.values()){
            if (paquetesEspectaculo.get(paq.getPaquete().getNombre())!=null) {
                paquetes.put(paq.getPaquete().getNombre(),paq.getPaquete());
            }
        }
        return paquetes;
    }
}

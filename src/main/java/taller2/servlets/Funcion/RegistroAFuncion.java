package taller2.servlets.FuncionDTO;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import main.java.taller1.Logica.Clases.*;
import main.java.taller1.Logica.DTOs.FuncionDTO;
import main.java.taller1.Logica.Fabrica;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "RegistroAFuncion", value = "/registro-espectadores-a-funcion")
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
                Map<String, Plataforma> todasPlataformas = fabrica.getIPlataforma().obtenerPlataformas();
                Map<String, Espectaculo> todosEspectaculos = fabrica.getIEspectaculo().obtenerEspectaculos();
                Map<String, Paquete> todosPaquetes = fabrica.getIPaquete().obtenerPaquetes();
                Map<String, Categoria> todasCategorias = fabrica.getICategoria().obtenerCategorias();
                Map<String, Usuario> todosUsuarios = fabrica.getIUsuario().obtenerUsuarios();
            
                request.setAttribute("todasPlataformas", todasPlataformas);
                request.setAttribute("todosEspectaculos", todosEspectaculos);
                request.setAttribute("todosPaquetes", todosPaquetes);
                request.setAttribute("todasCategorias", todasCategorias);
                request.setAttribute("todosUsuarios", todosUsuarios);
            
                HttpSession session = request.getSession();
                boolean esEspectador= (boolean) session.getAttribute("esEspectador");
                if(esEspectador) {
                    String espectaculo = request.getParameter("espectaculo");
                    String funcion = request.getParameter("nombre");
                    String plataforma = request.getParameter("plataforma");
                    Espectador esp=(Espectador)request.getSession().getAttribute("usuarioLogueado");
                    String nombreEsp=esp.getNickname();
                    FuncionDTO fun = fabrica.getIFuncion().obtenerFuncion(plataforma, espectaculo, funcion).get();
                    Map<String, EspectadorRegistradoAFuncion> registros = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(nombreEsp);
                    //Obtengo los paquetes del espectador que tienen el espectaculo asociado
                    Map<String, Paquete> paquetes = obtenerPaquetesEspectadorEspectaculo(espectaculo, plataforma, nombreEsp);
                    
                    request.setAttribute("funcion", fun);
                    request.setAttribute("registros", registros);
                    request.setAttribute("paquetes", paquetes);
                    dispatchPage("/pages/funcion/registro-espectadores-a-funcion.jsp", request, response);
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
        String espectaculo = request.getParameter("espectaculo");
        String funcion = request.getParameter("funcion");
        String plataforma = request.getParameter("plataforma");
        String paquete = request.getParameter("paquete");
        //String espectador = "Domainer";
        Espectador esp=(Espectador)request.getSession().getAttribute("usuarioLogueado");
        String espectador=esp.getNickname();
        double costo=0;
        String[] registrosCanjeados = request.getParameterValues("registrosCanjeados");

        //Espectador esp= (Espectador) fabrica.getIUsuario().obtenerUsuarios().get(espectador);
        FuncionDTO fun = (fabrica.getIFuncion().obtenerFuncion(plataforma, espectaculo, funcion).get());
        Map<String, EspectadorRegistradoAFuncion> registros = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(espectador);
        Map<String, Paquete> paquetes=obtenerPaquetesEspectadorEspectaculo(espectaculo,plataforma,espectador);
        Paquete paq=null;

        request.setAttribute("funcion",fun);
        request.setAttribute("registros",registros);
        request.setAttribute("paquetes", paquetes);

        Map<String, EspectadorRegistradoAFuncion> FuncionesCanjeadas = new HashMap<>();
        int cantMaxEspect = fabrica.getIFuncion().obtenerEspectadoresRegistradosAFuncion(funcion).size();

        if (cantMaxEspect == fun.getEspectaculo().getMaxEspectadores()) {
            dispatchError("No se puede registrar, cantidad maxima alcanzada", request, response);
        } else if (fabrica.getIFuncion().obtenerEspectadoresRegistradosAFuncion(funcion).get(espectador) != null) {
            dispatchError("No se puede, ya esta registrado", request, response);
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
                    dispatchError("La cantidad de registros debe ser tres(3) para que el costo sea 0", request, response);
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
                dispatchError("No se pudo registrar a la funcion", request, response);
            }
        }
    }

    private Map<String,Paquete> obtenerPaquetesEspectadorEspectaculo(String espectaculo,String plataforma, String espectador ){
        Map<String, Paquete> paquetesEspectaculo = fabrica.getIPaquete().obtenerPaquetesDeEspectaculo(espectaculo,plataforma);
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

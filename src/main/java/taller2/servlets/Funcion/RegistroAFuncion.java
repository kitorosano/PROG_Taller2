package taller2.servlets.Funcion;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import taller2.DTOs.*;
import taller2.utils.Fetch;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegistroAFuncion", value = "/registro-espectadores-a-funcion")
public class RegistroAFuncion extends HttpServlet {
    
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
        RequestDispatcher view = request.getRequestDispatcher("/pages/funcion/registro-espectadores-a-funcion.jsp");
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
            
                HttpSession session = request.getSession();
                boolean esEspectador= (boolean) session.getAttribute("esEspectador");
                if(esEspectador) {
                    String espectaculo = request.getParameter("espectaculo");
                    String funcion = request.getParameter("nombre");
                    String plataforma = request.getParameter("plataforma");
                    //Espectador esp=(Espectador)request.getSession().getAttribute("usuarioLogueado");
                    UsuarioDTO esp= (UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");
                    String nombreEsp=esp.getNickname();
                    //FuncionDTO fun = fabrica.getIFuncion().obtenerFuncion(plataforma, espectaculo, funcion).get();
                    FuncionDTO fun= fetch.Set("/funciones/find?nombrePlataforma="+plataforma+"&nombreEspectaculo="+espectaculo+"&nombreFuncion="+funcion).Get().getFuncion();
                    //Map<String, EspectadorRegistradoAFuncionDTO> registros = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(nombreEsp);
                    Map<String, EspectadorRegistradoAFuncionDTO> registros= fetch.Set("/espectadorRegistradoAFuncion/findByNickname?nickname="+nombreEsp).Get().getMapEspectadorRegistradoAFuncion();
                    //Obtengo los paquetes del espectador que tienen el espectaculo asociado
                    Map<String, PaqueteDTO> paquetes = obtenerPaquetesEspectadorEspectaculo(espectaculo, plataforma, nombreEsp);
                    
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
        UsuarioDTO esp=(UsuarioDTO) request.getSession().getAttribute("usuarioLogueado");
        String espectador=esp.getNickname();
        double costo=0;
        String[] registrosCanjeados = request.getParameterValues("registrosCanjeados");
        
        try{
    
            //Espectador esp= (Espectador) fabrica.getIUsuario().obtenerUsuarios().get(espectador);
            //FuncionDTO fun = (fabrica.getIFuncion().obtenerFuncion(plataforma, espectaculo, funcion).get());
            FuncionDTO fun= fetch.Set("/funciones/find?nombrePlataforma="+plataforma+"&nombreEspectaculo="+espectaculo+"&nombreFuncion="+funcion).Get().getFuncion();
            Map<String, EspectadorRegistradoAFuncionDTO> registros= fetch.Set("/espectadorRegistradoAFuncion/findByNickname?nickname="+espectador).Get().getMapEspectadorRegistradoAFuncion();
            Map<String, PaqueteDTO> paquetes=obtenerPaquetesEspectadorEspectaculo(espectaculo,plataforma,espectador);
            PaqueteDTO paq=null;
    
            request.setAttribute("funcion",fun);
            request.setAttribute("registros",registros);
            request.setAttribute("paquetes", paquetes);
    
            Map<String, EspectadorRegistradoAFuncionDTO> FuncionesCanjeadas = new HashMap<>();
            //int cantMaxEspect = fabrica.getIFuncion().obtenerEspectadoresRegistradosAFuncion(funcion).size();
            Map<String,UsuarioDTO> espectadores= fetch.Set("/espectadorRegistradoAFuncion/findByFuncion?nombreFuncion="+funcion).Get().getMapUsuario();
            int cantMaxEspect=espectadores.size();
    
            if (cantMaxEspect == fun.getEspectaculo().getMaxEspectadores()) {
                dispatchError("No se puede registrar, cantidad maxima alcanzada", request, response);
            } else if (espectadores.get(espectador) != null) {
                dispatchError("No se puede, ya esta registrado", request, response);
            } else {
                if (registrosCanjeados != null) {
                    if (registrosCanjeados.length == 3) {
                        Map<String, EspectadorRegistradoAFuncionDTO> funciones = fetch.Set("/espectadorRegistradoAFuncion/findByNickname?nickname=" + espectador).Get().getMapEspectadorRegistradoAFuncion();
                        for (String registro : registrosCanjeados) {
                            //EspectadorRegistradoAFuncionDTO canjeada = fabrica.getIFuncion().obtenerFuncionesRegistradasDelEspectador(espectador).get(registro);
                            EspectadorRegistradoAFuncionDTO canjeada = funciones.get(registro);
                            canjeada.setCanjeado(true);
                            FuncionesCanjeadas.put(canjeada.getFuncion().getNombre(), canjeada);
                        }
                        costo = 0;
                    } else {
                        dispatchError("La cantidad de registros debe ser tres(3) para que el costo sea 0", request, response);
                    }
                } else {
                    costo = fun.getEspectaculo().getCosto();
                }
                if (!paquete.equals("undefined") && costo != 0) {
                    Map<String, PaqueteDTO> paquetesEspectador = fetch.Set("/paquetes/findByNombreEspectador?nombreEspectador=" + espectador).Get().getMapPaquete();
                    //paq = fabrica.getIPaquete().obtenerPaquetesPorEspectador(espectador).get(paquete).getPaquete();
                    paq = paquetesEspectador.get(paquete);
                    costo = fun.getEspectaculo().getCosto() - (fun.getEspectaculo().getCosto() * paq.getDescuento() / 100);
                }
                AltaEspectadorRegistradoAFuncionDTO nuevo = new AltaEspectadorRegistradoAFuncionDTO();
                nuevo.setEspectador(esp.getNickname());
                nuevo.setFuncion(fun.getNombre());
                nuevo.setEspectaculo(fun.getEspectaculo().getNombre());
                nuevo.setPlataforma(fun.getEspectaculo().getPlataforma().getNombre());
                nuevo.setCanjeado(false);
                nuevo.setFechaRegistro(LocalDateTime.now().toString());
                nuevo.setCosto(costo);
                if(paq!=null){
                    nuevo.setPaquete(paq.getNombre());
                }else{
                    nuevo.setPaquete(null);
                }
                fetch.Set("/espectadorRegistradoAFuncion/create", nuevo).Post();
                //fabrica.getIFuncion().registrarEspectadorAFuncion(nuevo);
                response.sendRedirect("home");
            }
        } catch (RuntimeException | IOException e) {
            System.out.println(e);
            dispatchError("No se pudo registrar a la funcion", request, response);
        }
    }

    private Map<String,PaqueteDTO> obtenerPaquetesEspectadorEspectaculo(String espectaculo,String plataforma, String espectador ){
        try {
            //Map<String, PaqueteDTO> paquetesEspectaculo = fabrica.getIPaquete().obtenerPaquetesDeEspectaculo(espectaculo,plataforma);
            Map<String, PaqueteDTO> paquetesEspectaculo= fetch.Set("/paquetes/findByEspectaculoAndPlataforma?nombreEspectaculo="+espectaculo+"&nombrePlataforma="+plataforma).Get().getMapPaquete();
            //Map<String, AltaEspectadorAPaqueteDTO> paquetesEspectador = fabrica.getIPaquete().obtenerPaquetesPorEspectador(espectador);
            Map<String, PaqueteDTO> paquetesEspectador= fetch.Set("/paquetes/findByNombreEspectador?nombreEspectador="+espectador).Get().getMapPaquete();
            Map<String, PaqueteDTO> paquetes= new HashMap<>();
            for(PaqueteDTO paq :paquetesEspectador.values()){
                if (paquetesEspectaculo.get(paq.getNombre())!=null) {
                    paquetes.put(paq.getNombre(),paq);
                }
            }
            return paquetes;
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

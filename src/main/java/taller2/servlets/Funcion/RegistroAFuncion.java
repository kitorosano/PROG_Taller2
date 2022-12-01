package taller2.servlets.Funcion;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import taller2.DTOs.*;
import taller2.utils.FetchApiOptions;
import taller2.utils.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegistroAFuncion", value = "/registro-espectadores-a-funcion")
public class RegistroAFuncion extends HttpServlet {


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
                    FuncionDTO fun= (FuncionDTO) Utils.FetchApi("/funciones?nombrePlataforma="+plataforma+"&nombreEspectaculo="+espectaculo+"&nombreFuncion="+funcion).getEntity();
                    //Map<String, EspectadorRegistradoAFuncionDTO> registros = Fabrica.getInstance().getIFuncion().obtenerFuncionesRegistradasDelEspectador(nombreEsp);
                    Map<String, EspectadorRegistradoAFuncionDTO> registros= (Map<String, EspectadorRegistradoAFuncionDTO>) Utils.FetchApi("/espectadorRegistradoAFuncion/findByNickname?nickname="+nombreEsp).getEntity();
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

        //Espectador esp= (Espectador) fabrica.getIUsuario().obtenerUsuarios().get(espectador);
        //FuncionDTO fun = (fabrica.getIFuncion().obtenerFuncion(plataforma, espectaculo, funcion).get());
        FuncionDTO fun= (FuncionDTO) Utils.FetchApi("funciones?nombrePlataforma="+plataforma+"nombreEspectaculo="+espectaculo+"nombreFuncion="+funcion).getEntity();
        Map<String, EspectadorRegistradoAFuncionDTO> registros= (Map<String, EspectadorRegistradoAFuncionDTO>) Utils.FetchApi("/espectadorRegistradoAFuncion/findByNickname?nickname="+espectador).getEntity();
        Map<String, PaqueteDTO> paquetes=obtenerPaquetesEspectadorEspectaculo(espectaculo,plataforma,espectador);
        PaqueteDTO paq=null;

        request.setAttribute("funcion",fun);
        request.setAttribute("registros",registros);
        request.setAttribute("paquetes", paquetes);

        Map<String, EspectadorRegistradoAFuncionDTO> FuncionesCanjeadas = new HashMap<>();
        //int cantMaxEspect = fabrica.getIFuncion().obtenerEspectadoresRegistradosAFuncion(funcion).size();
        Map<String,UsuarioDTO> espectadores= (Map<String,UsuarioDTO>) Utils.FetchApi("/espectadorRegistradoAFuncion/findByFuncion?nombreFuncion="+funcion).getEntity();
        int cantMaxEspect=espectadores.size();

        if (cantMaxEspect == fun.getEspectaculo().getMaxEspectadores()) {
            dispatchError("No se puede registrar, cantidad maxima alcanzada", request, response);
        } else if (espectadores.get(espectador) != null) {
            dispatchError("No se puede, ya esta registrado", request, response);
        } else {
            if (registrosCanjeados != null) {
                if (registrosCanjeados.length == 3) {
                    Map<String,EspectadorRegistradoAFuncionDTO> funciones= (Map<String, EspectadorRegistradoAFuncionDTO>) Utils.FetchApi("/espectadorRegistradoAFuncion/findByNickname?nickname="+espectador).getEntity();
                    for (String registro : registrosCanjeados) {
                        //EspectadorRegistradoAFuncionDTO canjeada = fabrica.getIFuncion().obtenerFuncionesRegistradasDelEspectador(espectador).get(registro);
                        EspectadorRegistradoAFuncionDTO canjeada=funciones.get(registro);
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
                Map<String, PaqueteDTO> paquetesEspectador= (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes/findByNombreEspectador?nombreEspectador="+espectador).getEntity();
                //paq = fabrica.getIPaquete().obtenerPaquetesPorEspectador(espectador).get(paquete).getPaquete();
                paq=paquetesEspectador.get(paquete);
                costo = fun.getEspectaculo().getCosto() - (fun.getEspectaculo().getCosto() * paq.getDescuento() / 100);
            }
            EspectadorRegistradoAFuncionDTO nuevo= new EspectadorRegistradoAFuncionDTO();
            nuevo.setEspectador(esp);
            nuevo.setFuncion(fun);
            nuevo.setPaquete(paq);
            nuevo.setCanjeado(true);
            nuevo.setFechaRegistro(LocalDateTime.now());
            try{
                String body =new Gson().toJson(nuevo);
                FetchApiOptions options= new FetchApiOptions("POST",body);
                Utils.FetchApi("/espectadorRegistradoAFuncion/create/",options);
                //fabrica.getIFuncion().registrarEspectadorAFuncion(nuevo);
                response.sendRedirect("home");
            } catch (Exception e) {
                System.out.println(e);
                dispatchError("No se pudo registrar a la funcion", request, response);
            }
        }
    }

    private Map<String,PaqueteDTO> obtenerPaquetesEspectadorEspectaculo(String espectaculo,String plataforma, String espectador ){
        //Map<String, PaqueteDTO> paquetesEspectaculo = fabrica.getIPaquete().obtenerPaquetesDeEspectaculo(espectaculo,plataforma);
        Map<String, PaqueteDTO> paquetesEspectaculo= (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes/findByspectaculoAndPlataforma?nombreEspectaculo="+espectaculo+"&nombrePlataforma="+plataforma);
        //Map<String, AltaEspectadorAPaqueteDTO> paquetesEspectador = fabrica.getIPaquete().obtenerPaquetesPorEspectador(espectador);
        Map<String, PaqueteDTO> paquetesEspectador= (Map<String, PaqueteDTO>) Utils.FetchApi("/paquetes/findByNombreEspectador?nombreEspectador="+espectador).getEntity();
        Map<String, PaqueteDTO> paquetes= new HashMap<>();
        for(PaqueteDTO paq :paquetesEspectador.values()){
            if (paquetesEspectaculo.get(paq.getNombre())!=null) {
                paquetes.put(paq.getNombre(),paq);
            }
        }
        return paquetes;
    }
}

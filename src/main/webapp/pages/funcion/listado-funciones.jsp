<%--
  Created by IntelliJ IDEA.
  User: sebas
  Date: 12/10/2022
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.DTOs.PlataformaDTO" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="main.java.taller1.Logica.Clases.Funcion" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="main.java.taller1.Logica.DTOs.PlataformaDTO" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    String filtroPlataforma = request.getParameter("filtroPlataforma") != null ? request.getParameter("filtroPlataforma") : "";
    String filtroEspectaculo = request.getParameter("filtroEspectaculo") != null ? request.getParameter("filtroEspectaculo") : "";
    Map<String, Funcion> funcionesFiltradas = request.getAttribute("funcionesFiltradas") != null ? (Map<String, Funcion>) request.getAttribute("funcionesFiltradas") : new HashMap<>();

%>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="../global.css" %></style>
    <style><%@ include file="/pages/funcion/listado-funciones.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CoronaTicketsUY</title>
</head>
<body>
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="/pages/header.jsp" %>
            <div class="page-title">
                <h3>Listado de funciones</h3>
            </div>
            <section>
                <%@ include file="/pages/sidebar.jsp" %>
                <div class="main-container">
                    <%-- AGREGAR COMPONENTES ABAJO--%>
                    <div class="plataformas-espectaculos-container" style="display: flex; flex-direction: column;" id="select_list">
                        <form method="GET" action="listado-funciones" id="formFunciones" style="display: flex; gap: 20px">
                            <div>
                                <label for="filtroPlataforma">Selecciona una plataforma:</label>
                                <select name="filtroPlataforma" id="filtroPlataforma">
                                    <option value="">Todas</option>
                                    <% for (PlataformaDTO plataforma : plataformasMap.values()) { %>
                                    <option value="<%= plataforma.getNombre() %>"><%= plataforma.getNombre() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <div>
                                <label for="filtroEspectaculo">Selecciona un espectáculo:</label>
                                <select name="filtroEspectaculo" id="filtroEspectaculo">
                                    <option value="">Todos</option>
                                    <% for (Espectaculo espectaculo : espectaculosMap.values()) { %>
                                        <option data-plataforma="<%= espectaculo.getPlataforma().getNombre() %>" value="<%= espectaculo.getNombre() %>"><%= espectaculo.getNombre() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <div style="float: right">
                                <button type="submit">Buscar</button>
                                <button onclick="resetForm()">Resetear</button>
                            </div>
                        </form>
                    </div>
            
                    <div class="busqueda">
                        <label for="txtBuscar">Filtrar funcion: </label>
                        <input type="text" name="buscarFuncion" id="txtBuscar" value="Funcion...">
                    </div>
            
                    <div>
                        <table class="tablaFunciones" id="tabla">
                            <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Plataforma</th>
                                <th>Espectaculo</th>
                                <th>Fecha de inicio</th>
                            </tr>
                            </thead>
                            <tbody id="cuerpoTabla">
                            </tbody>
                        </table>
                    </div>
                    <%-- AGREGAR COMPONENTES ARRIBA--%>
                </div>
            
            </section>
        </main>
    </div>
    
    <%--    Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        // Declaramos elementos del DOM
        const TABLA = document.getElementById("cuerpoTabla");
        const FORM = document.getElementById("formFunciones");
        const SELECT_PLATAFORMA = document.getElementById("filtroPlataforma");
        const SELECT_ESPECTACULO = document.getElementById("filtroEspectaculo");
        const TXT_BUSCAR = $("#txtBuscar");
    
        //CUANDO EL DOCUMENTO ESTE LISTO
        $(document).ready(function(){
            seleccionarFiltrosAnteriores();
            filtrarEspectaculos("<%= filtroPlataforma %>");
            crearTabla();
        });
    
        function resetForm() {
            FORM.reset();
        }
    
        function seleccionarFiltrosAnteriores() {
            const opcionPlataforma = "<%= filtroPlataforma %>";
            for (let i = 0; i < SELECT_PLATAFORMA.options.length; i++) {
                if (SELECT_PLATAFORMA.options[i].value === opcionPlataforma) {
                    SELECT_PLATAFORMA.options[i].selected = true;
                    break;
                }
            }
            const optionsEspectaculo = SELECT_ESPECTACULO.options;
            for (let i = 0; i < optionsEspectaculo.length; i++) {
                let option = optionsEspectaculo[i];
                if (option.value === "<%= filtroEspectaculo %>") {
                    option.selected = true;
                }
            }
        }
    
        SELECT_PLATAFORMA.addEventListener("change", function (e) {
            filtrarEspectaculos(e.target.value);
        });
        SELECT_ESPECTACULO.addEventListener("change", function (e) {
            if (!(SELECT_ESPECTACULO.value === "")) {
                SELECT_PLATAFORMA.value = e.target.options[e.target.selectedIndex].dataset.plataforma;
            }
        });
    
        function filtrarEspectaculos(plataformaSeleccionada) {
            SELECT_ESPECTACULO.options[0].style.display = "block" // El primero siempre va, ya que es la opcion de "Todas"

            for (let i = 1; i < SELECT_ESPECTACULO.options.length; i++) {
                let option = SELECT_ESPECTACULO.options[i];
    
                // Si no hay plataforma seleccionada, mostramos todas las opciones y seleccionamos la primera
                if (plataformaSeleccionada === ""){
                    SELECT_ESPECTACULO.options[0].selected = true;
                    option.style.display = "block";
                }

                // Si la plataforma de la opcion es igual a la seleccionada, mostramos la opcion
                else if (option.dataset.plataforma === plataformaSeleccionada) {
                    option.style.display = "block";
                } else {
                    option.style.display = "none";
                }
            }
        }
    
        function crearTabla(){
            let nuevaFila;
            let celdaNombre;
            let celdaPlataforma;
            let celdaEspectaculo;
            let celdaInicio;
    
            <%for (Funcion elem : funcionesFiltradas.values()) {%>
                nuevaFila = TABLA.insertRow(-1);
                celdaNombre = nuevaFila.insertCell(0);
                celdaPlataforma = nuevaFila.insertCell(1);
                celdaEspectaculo = nuevaFila.insertCell(2);
                celdaInicio = nuevaFila.insertCell(3);
    
                celdaNombre.innerHTML = "<%=elem.getNombre()%>";
                celdaPlataforma.innerHTML = "<%=elem.getEspectaculo().getPlataforma().getNombre()%>";
                celdaEspectaculo.innerHTML = "<%=elem.getEspectaculo().getNombre()%>";
                celdaInicio.innerHTML = "<%=elem.getFechaHoraInicio()%>";
                nuevaFila.addEventListener("click", () => {
                    window.location.href = "detalle-funcion?nombre=<%=elem.getNombre()%>&espectaculo=<%=elem.getEspectaculo().getNombre()%>&plataforma=<%=elem.getEspectaculo().getPlataforma().getNombre()%>";
                });
            <% } %>
        }
    
        //FUNCION PARA BUSCAR POR FUNCIONES EN TIEMPO REAL
        TXT_BUSCAR.on("keyup", function() {
            let keyword = this.value;
            keyword = keyword.toUpperCase();
            let table_1 = document.getElementById("tabla");
            let all_tr = table_1.getElementsByTagName("tr");
            for(let i=0; i<all_tr.length; i++){
                let name_column = all_tr[i].getElementsByTagName("td")[0];
                if(name_column){
                    let name_value = name_column.textContent || name_column.innerText;
                    name_value = name_value.toUpperCase();
                    if(name_value.indexOf(keyword) > -1){
                        all_tr[i].style.display = ""; // show
                    }else{
                        all_tr[i].style.display = "none"; // hide
                    }
                }
            }
        });
    
        //limpiar textbox
        TXT_BUSCAR.click(function(){
            TXT_BUSCAR.val("");
        });
    
    </script>
</body>
</html>
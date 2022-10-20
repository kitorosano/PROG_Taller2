<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %><%--
  Created by IntelliJ IDEA.
  User: sebas
  Date: 12/10/2022
  Time: 18:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.Plataforma" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Espectaculo" %>
<%@ page import="main.java.taller1.Logica.Clases.Funcion" %>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="../global.css" %></style>
    <style><%@ include file="listado-funciones.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="../header.jsp" %>

<section>
    <%@ include file="../sidebar.jsp" %>
    <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
        <div class="plataformas-espectaculos-container" style="display: flex; flex-direction: column;">
            <form method="GET" action="listado-funciones" id="recargar">
                <button type="button" onclick="resetearForm()">Resetear</button>
            </form>
            <br>
            <form method="POST" action="listado-funciones" id="formFunciones" >
                <label for="plataforma">Selecciona una plataforma:</label>
                <select name="plataforma" id="plataforma">
                    <option value="" selected disabled hidden>Plataforma</option>
                </select>
                <br> <br>
                <label for="espectaculo">Selecciona un espectáculo:</label>
                <select name="espectaculo" id="espectaculo">
                    <option value="" selected disabled hidden>Espectaculo</option>
                </select>
                <button type="button" onclick="enviarForm()">Buscar</button>
            </form>
        </div>

        <div class="busqueda">
            <br/>
            <label for="txtBuscar">Buscar funcion</label>
            <input type="text" name="buscarFuncion" id="txtBuscar" value="Funcion...">
        </div>

        <div>
            <h2>Funciones</h2>
            <table class="tablaFunciones" id="tabla">
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Fecha de inicio</th>
                </tr>
                </thead>
                <tbody id="cuerpoTabla">
                </tbody>
            </table>
        </div>
        <%--                AGREGAR COMPONENTES ACA--%>
    </div>
</section>

<script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
<script>

    //CUANDO EL DOCUMENTO ESTE LISTO
    $(document).ready(function(){
        cargarPlataformas();
        <%if (request.getAttribute("espectaculos") != null){%>
            cargarEspectaculos();
            <%}%>
        <%if (request.getAttribute("funciones") != null){%>
        crearTablaTotal();
        <%}%>
    });

    function enviarForm(){
        $("#formFunciones").first().submit();
    }
    function resetearForm(){
        $("#recargar").first().submit();
    }

    //limpiar textbox
    $("#txtBuscar").click(function(){
        $("#txtBuscar").val("");
    });

    $("#plataforma").on("change", function() {
        document.getElementById("espectaculo").textContent = "";
        $("#formFunciones").first().submit();
    });

    //FUNCION PARA BUSCAR POR FUNCIONES EN TIEMPO REAL
    $("#txtBuscar").on("keyup", function() {
        var keyword = this.value;
        keyword = keyword.toUpperCase();
        var table_1 = document.getElementById("tabla");
        var all_tr = table_1.getElementsByTagName("tr");
        for(var i=0; i<all_tr.length; i++){
            var name_column = all_tr[i].getElementsByTagName("td")[0];
            if(name_column){
                var name_value = name_column.textContent || name_column.innerText;
                name_value = name_value.toUpperCase();
                if(name_value.indexOf(keyword) > -1){
                    all_tr[i].style.display = ""; // show
                }else{
                    all_tr[i].style.display = "none"; // hide
                }
            }
        }
    });

    function cargarPlataformas(){
        <%Map<String, Plataforma> plataformas = (Map<String, Plataforma>) request.getAttribute("plataformas");%>
        let select = document.getElementById("plataforma");
        let opt;
        <%for (Plataforma elem : plataformas.values()) {%>
        opt = document.createElement('option');
        opt.value = "<%=elem.getNombre()%>";
        <%
        String s2 = elem.getNombre();
        String s1 = request.getParameter("plataforma") instanceof String ? request.getParameter("plataforma") : "";
        if (s1.equals(s2)){%>
        opt.selected="selected"
        <%}%>
        opt.innerHTML = "<%=elem.getNombre()%>";
        select.appendChild(opt);
        <% } %>
    }

    <% if (request.getAttribute("funciones") != null){ %>
    function crearTablaTotal(){
        <%Map<String, Funcion> totalFunciones = (Map<String, Funcion>) request.getAttribute("funciones");%>

        let tabla = document.getElementById("cuerpoTabla");
        let nuevaFila = document.createElement("tr");
        let celdaNombre = document.createElement("td");
        let celdaInicio = document.createElement("td");

        <%for (Funcion elem : totalFunciones.values()) {%>
        nuevaFila = tabla.insertRow(-1);
        celdaNombre = nuevaFila.insertCell(0);
        celdaInicio = nuevaFila.insertCell(1);

        celdaNombre.innerHTML = "<%=elem.getNombre()%>";
        celdaNombre.setAttribute('onClick',"location.href='detalle-espectaculo?nombre_funcion=<%=elem.getNombre()%>&nombre_plataforma=<%=request.getAttribute("plataforma")%>&nombre_espectaculo=<%=request.getAttribute("espectaculo")%>'");
        celdaInicio.innerHTML = "<%=elem.getFechaHoraInicio()%>";
        <% } %>
    }
    <% } if (request.getAttribute("espectaculos") != null){ %>
    function cargarEspectaculos(){
        <%Map<String, Espectaculo> espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculos");%>
        let select = document.getElementById("espectaculo");
        let opt;
        <%for (Espectaculo elem : espectaculos.values()) {%>
            opt = document.createElement('option');
            opt.value = "<%=elem.getNombre()%>";
            <%
            String s2 = elem.getNombre();
            String s1 = request.getParameter("espectaculo") instanceof String ? request.getParameter("espectaculo") : "";
            if (s1.equals(s2)){%>
                opt.selected="selected"
            <%}%>
            opt.innerHTML = "<%=elem.getNombre()%>";
            select.appendChild(opt);
        <% } %>
        }
    <% } %>

</script>
</body>
</html>
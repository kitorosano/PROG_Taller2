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
<% String nickname = request.getParameter("nickname") instanceof String ? request.getParameter("nickname") : ""; %>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="./global.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="header.jsp" %>

<section>
    <%@ include file="sidebar.jsp" %>
    <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
        <div class="plataformas-espectaculos-container" style="display: flex; flex-direction: row;">
            <form method="POST" action="listado-funciones" id="formFunciones">
                <label for="plataforma">Selecciona una plataforma:</label>
                <select name="plataforma" id="plataforma">
                    <%
                        Map<String, Plataforma> plataformas= (Map<String, Plataforma>) request.getAttribute("plataformas");
                        for (Plataforma elem : plataformas.values()) {
                    %>
                    <option value="<%=elem.getNombre()%>"><%=elem.getNombre()%></option>
                    <% } %>
                </select>
                <label for="espectaculos">Selecciona un espectáculo:</label>
                <select name="espectaculos" id="espectaculos">
                    <%
                        Map<String, Espectaculo> espectaculos = (Map<String, Espectaculo>) request.getAttribute("espectaculos");
                        for (Espectaculo elem : espectaculos.values()) {
                    %>
                    <option value="<%=elem.getNombre()%>"><%=elem.getNombre()%></option>
                    <% } %>
                </select>
                <button type="button" onclick="enviarForm()">Buscar</button>
            </form>

            <form method="GET" action="listado-espectaculos" id="resetEspectaculos">
                <button type="button" onclick="resetearForm()">Resetear</button>
            </form>


        </div>

        <div class="busqueda">
            <br/>
            <label for="">Buscar espectaculo</label>
            <input type="text" name="buscarFuncion" id="txtBuscar" value="Funcion...">
        </div>

        <div>
            <h2>Espectaculos</h2>
            <table class="tablaFunciones" id="tabla">
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Descripción</th>
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
        <%if (request.getAttribute("totalFunciones") != null){%>
        crearTablaTotal();
        <%}else{%>
        crearTablaConsulta();
        <%}%>
    });

    function enviarForm(){
        $("#formFunciones").first().submit();
    }
    function resetearForm(){
        $("#formFunciones").first().submit();
    }

    //SI EL ATRIBUTO DEL REQUEST "totalEspectaculos" NO SE ENCUENTRA NULO, ENTONCES LA PETICION FUE UN DOGET()
    <% if (request.getAttribute("totalFunciones") != null){ %>
    function crearTablaTotal(){
        <%Map<String, Funcion> totalFunciones = (Map<String, Funcion>) request.getAttribute("totalFunciones");%>

        let tabla = document.getElementById("cuerpoTabla");
        let nuevaFila = document.createElement("tr");
        let celdaNombre = document.createElement("td");
        let celdaInicio = document.createElement("td");

        <%for (Funcion elem : totalFunciones.values()) {%>
        nuevaFila = tabla.insertRow(-1);
        celdaNombre = nuevaFila.insertCell(0);
        celdaInicio = nuevaFila.insertCell(1);

        celdaNombre.innerHTML = "<%=elem.getNombre()%>";
        celdaInicio.innerHTML = "<%=elem.getFechaHoraInicio()%>";

        //celdaEspectaculo.setAttribute('onClick','window.location.href = \'detalle-espectaculo.jsp\';');

        <% } %>
    }
    <% }else{ %> //SI EL ATRIBUTO DEL REQUEST "totalEspectaculos" SE ENCUENTRA NULO, ENTONCES LA PETICION FUE UN DOPOST()
    function crearTablaConsulta(){
        <%Map<String, Funcion> totalFunciones = (Map<String, Funcion>) request.getAttribute("funcionesConsulta");%>

        let tabla = document.getElementById("cuerpoTabla");
        let nuevaFila = document.createElement("tr");
        let celdaNombre = document.createElement("td");
        let celdaInicio = document.createElement("td");

        <%for (Funcion elem : totalFunciones.values()) {%>
        nuevaFila = tabla.insertRow(-1);
        celdaNombre = nuevaFila.insertCell(0);
        celdaInicio = nuevaFila.insertCell(1);

        celdaNombre.innerHTML = "<%=elem.getNombre()%>";
        celdaInicio.innerHTML = "<%=elem.getFechaHoraInicio()%>";

        //celdaEspectaculo.setAttribute('onClick','window.location.href = \'detalle-espectaculo.jsp\';');
        <% } %>
    }
    <% } %>

    //FUNCION PARA BUSCAR POR ESPECTACULOS EN TIEMPO REAL
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
    //limpiar textbox
    $("#txtBuscar").click(function(){
        $("#txtBuscar").val("");
    });

</script>
</body>
</html>
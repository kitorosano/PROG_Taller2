<%@ page import="main.java.taller1.Logica.Clases.Paquete" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Map<String, Paquete> paquetes = request.getAttribute("paquetes") != null ? (Map<String, Paquete>) request.getAttribute("paquetes") : null;
    String json = new Gson().toJson(paquetes);
%>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="../global.css" %></style>
    <style><%@ include file="/pages/paquete/listado-paquetes.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
    <%@ include file="/pages/header.jsp" %>
    
    <section>
        <%@ include file="/pages/sidebar.jsp" %>
        <div class="main-container">
            <%-- AGREGAR COMPONENTES ABAJO--%>
            <div class="busqueda">
                <label for="txtBuscar">Buscar paquete</label>
                <input type="text" name="buscarPaquete" id="txtBuscar" value="Nombre...">
            </div>
            <div>
                <h2>Paquetes</h2>
                <button class="volver" onclick="history.back()">Volver</button>
                <table class="tablaPaquetes" id="tabla">
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
            <%-- AGREGAR COMPONENTES ARRIBA--%>
        </div>
    </section>
    
    <%--    Javascript--%>
    <script src="https://code.jquery.com/jquery-3.6.1.min.js" integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=" crossorigin="anonymous"></script>
    <script>
        // Declaramos elementos del DOM
        const TABLA = document.getElementById("cuerpoTabla");
        const TXT_BUSCAR = $("#txtBuscar");
    
        //CUANDO EL DOCUMENTO ESTE LISTO
        $(document).ready(function(){
            crearTabla();
        });
    
    
        function crearTabla(){
            let nuevaFila;
            let celdaNombre;
            let celdaDescripcion;
    
            <% for (Paquete elem : paquetes.values()) {%>
                nuevaFila = TABLA.insertRow(-1);
                celdaNombre = nuevaFila.insertCell(0);
                celdaDescripcion = nuevaFila.insertCell(1);
    
                celdaNombre.innerHTML = "<%=elem.getNombre()%>";
                celdaDescripcion.innerHTML = "<%=elem.getDescripcion()%>";
    
                nuevaFila.addEventListener("click", function(){
                    window.location.href = "detalle-paquete?nombre=<%=elem.getNombre()%>";
                });
            <% } %>
        }
    
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
    
        TXT_BUSCAR.click(function(){
            TXT_BUSCAR.val("");
        });
    </script>
</body>
</html>
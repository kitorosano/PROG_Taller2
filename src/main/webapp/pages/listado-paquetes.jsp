<%@ page import="main.java.taller1.Logica.Clases.Paquete" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="./global.css" %></style>
    <style><%@ include file="./listado-paquetes.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="header.jsp" %>

<section>
    <%@ include file="sidebar.jsp" %>
    <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
            <h2>Paquetes</h2>
            <br>
            <div>
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
        <%--                AGREGAR COMPONENTES ACA--%>
    </div>
</section>

<script>

    <% Map<String, Paquete> paquetes = (Map<String, Paquete>) request.getAttribute("paquetes");
    for (Paquete elem : paquetes.values()) {%>
    nuevaFila = tabla.insertRow(-1);
    celdaNombre = nuevaFila.insertCell(0);
    celdaDescripcion = nuevaFila.insertCell(1);

    celdaNombre.innerHTML = "<%=elem.getNombre()%>";
    celdaDescripcion.innerHTML = "<%=elem.getDescripcion()%>";

    celdaNombre.setAttribute('onClick',"location.href='detalle-paquete?nombre_paquete=<%=elem.getNombre()%>'");
    //celdaNombre.setAttribute('onClick','window.location.href = \'detalle-paquete.jsp\';');
    <% } %>

</script>
</body>
</html>
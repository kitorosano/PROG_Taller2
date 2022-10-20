<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page import="java.util.Map" %>
<%@ page import="main.java.taller1.Logica.Clases.Artista" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="/pages/global.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP - Hello World</title>
</head>
<body>
<%@ include file="/pages/header.jsp" %>

<section>
    <%@ include file="/pages/sidebar.jsp" %>
    <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
        <h2>Usuarios</h2>
        <br>
        <div>
            <table class="tablaUsuarios" id="tabla">
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Tipo de usuario</th>
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
    <% Map<String, Usuario> usuarios = (Map<String, Usuario>) request.getAttribute("usuarios");
    for (Usuario elem : usuarios.values()) {%>
    nuevaFila = tabla.insertRow(-1);
    celdaNickname = nuevaFila.insertCell(0);
    celdaTipo = nuevaFila.insertCell(1);

    celdaNickname.innerHTML = "<%=elem.getNickname()%>";
    <% if (elem instanceof Artista){%>
    celdaTipo.innerHTML = "Artista";
    <%}else{%>
    celdaTipo.innerHTML = "Espectador";
    <%}%>

    celdaNickname.setAttribute('onClick',"location.href='detalle-usuario?nickname=<%=elem.getNickname()%>'");
    <% } %>
</script>
</body>
</html>
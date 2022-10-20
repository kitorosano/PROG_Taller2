<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
                <h1><%= "Hello World!" + session.getAttribute("nickname") %>
                </h1>
                <br/>
                <a href="registro">Alta Usuario</a>
                <a href="listado-usuario">Listado Usuario</a>
                <a href="detalle-usuario">Detalle Usuario</a>
                <a href="alta-paquete">Alta Paquete</a>
                <a href="listado-paquetes">Listado Paquetes</a>
<%--                AGREGAR COMPONENTES ACA--%>
            </div>
        </section>
    </body>
</html>
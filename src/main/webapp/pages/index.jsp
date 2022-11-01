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
                <a href="listado-usuario">Listado Usuario</a>
                <a href="perfil">Perfil</a>
                <a href="alta-espectaculo">Alta Espectaculo</a>
                <a href="listado-espectaculos">Listado Espectáculo</a>
                <a href="alta-funcion">Registro de funcion</a>
                <a href="listado-funciones">Listado Funciones</a>
                <a href="alta-paquete">Alta Paquete</a>
                <a href="listado-paquetes">Listado Paquetes</a>
                <a href="espectaculoAPaquete?paquete=sixPack">Agregar Espectaculo a paquete</a>
                <a href='registroAFuncion?nombre=Casados+y+complicated&nombre_espectaculo=La+boda+de+tu+tia&nombre_plataforma=Twitch'>RegistroAFuncion</a>
<%--                AGREGAR COMPONENTES ACA--%>
            </div>
        </section>
    </body>
</html>
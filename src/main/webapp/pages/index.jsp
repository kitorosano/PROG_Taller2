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
                <a href="perfil">Perfil</a>
                <a href="registro-espectaculo">Alta de Espectaculo</a>
                <a href="registro-funcion">Alta de funcion</a>
                <a href="registro-paquete">Alta de Paquete</a>
                <a href="registro-espectaculo-a-paquete?paquete=sixPack">Agregar Espectaculo a paquete</a>
                <a href='registro-espectadores-a-funcion?nombre=Casados+y+complicated&nombre_espectaculo=La+boda+de+tu+tia&nombre_plataforma=Twitch'>RegistroAFuncion</a>
                <a href="listado-usuario">Listado de Usuarios</a>
                <a href="listado-espectaculos">Listado de Espectáculos</a>
                <a href="listado-funciones">Listado de Funciones</a>
                <a href="listado-paquetes">Listado de Paquetes</a>
<%--                AGREGAR COMPONENTES ACA--%>
            </div>
        </section>
    </body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>

<%  // Cargamos el usuarioLogueado en cada pantalla
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    
    String message = request.getAttribute("message") instanceof String ? (String) request.getAttribute("message") : "";
    String messageType = request.getAttribute("messageType") instanceof String ? (String) request.getAttribute("messageType") : "";
    
    Boolean esArtista = (Boolean) session.getAttribute("esArtista");
%>
<!DOCTYPE html>
<html>
<head>
    <style><%@ include file="./global.css" %></style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CoronaTicketsUY</title>
</head>
<body>
    <img class="background_img" src="https://i.imgur.com/d6cWesT.jpeg" alt="background_img"  />
    <div class="background_container">
        <div id="message" class="hidden <%=messageType%>" role="alert">
            <%=message%>
        </div>
        
        <main class="coronaTicketsUY">
            <%@ include file="header.jsp" %>
        
            <section>
                <%@ include file="sidebar.jsp" %>
                <div class="main-container">
        <%--                AGREGAR COMPONENTES ACA--%>
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
        </main>
    </div>
</body>
</html>
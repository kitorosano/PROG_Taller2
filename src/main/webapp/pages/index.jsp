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
            <div class="page-title">
                <h3>Bienvenido a CoronaTickets.uy!</h3>
            </div>
            <section>
                <div class="main-container">
                    <%-- AGREGAR COMPONENTES ABAJO--%>
                    <article class="content-area">
                        <div class="content-area-header">
                            <h2><a href="listado-espectaculos">Espectaculos</a></h2>
                            <span><a href="listado-espectaculos">Ver más</a></span>
                        </div>
                        <div class="content-area-content">
                        
                        </div>
                    </article>
                    <%-- AGREGAR COMPONENTES ARRIBA--%>
                </div>
                <%@ include file="sidebar.jsp" %>
                
<%--
                    <a href="registro-espectaculo-a-paquete?paquete=sixPack">Agregar Espectaculo a paquete</a>
                    <a href='registro-espectadores-a-funcion?nombre=Casados+y+complicated&nombre_espectaculo=La+boda+de+tu+tia&nombre_plataforma=Twitch'>RegistroAFuncion</a>
--%>
            </section>
        </main>
    </div>
</body>
</html>
<%--
  Created by IntelliJ IDEA.
  User: esteban.rosano
  Date: 12/10/2022
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.taller1.Logica.Clases.Usuario" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    Boolean esArtista = (Boolean) session.getAttribute("esArtista");
%>
<!DOCTYPE html>
<div class="header">
    <div class="header__left">
        <a href="<%= request.getContextPath()%>" class="header_title"><span>CoronaTickets</span>.uy</a>
    </div>
    
    <div class="header__right">
        <%-- TODO: HACER DESPLEGABLE DE LA IMAGEN, OPCIONES PERFIL Y CERRAR SESION--%>
        <a href="perfil">
            <%= usuarioLogueado.getNickname() %>
        </a>
        <img src="<%= usuarioLogueado.getImagen()%>" alt="user_img">
        <div class="vertical"></div>
        <form action="home" method="post">
            <button type="submit" class="header__logout" > Cerrar Sesion</button>
        </form>
    </div>
</div>